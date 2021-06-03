package com.elihimas.orchestra

import android.app.Activity
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.animations.*
import com.elihimas.orchestra.blocks.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

suspend fun coroutineDelay(millis: Long) = delay(millis)

interface ParallelContext

interface OrchestraContext {
    fun delay(duration: Long)
    fun delay(duration: Int)
    fun on(vararg views: View): ViewReference
    fun parallel(block: Orchestra.() -> Unit): ParallelContext
    fun then(block: () -> Unit)
    fun animations(): Animations
    fun changeConstrains(root: ConstraintLayout, layoutId: Int): ChangeConstrainsBlock
}

open class Orchestra : OrchestraContext, ParallelContext {

    private val ticker = AnimationTicker()

    private var lastParallelBlock: ParallelBlock? = null

    internal val blocks = LinkedList<Block>()
    private val executionLatch = CountDownLatch(1)

    internal fun runBlocks() {
        ticker.start(blocks)
    }

    internal suspend fun doRunBlocks(blocks: List<Block>, blocksLatch: CountDownLatch? = null) {
        blocks.forEach { block ->
            block.runBlock(this)
            blocksLatch?.countDown()
        }
    }

    override fun animations() = Animations()

    override fun on(vararg views: View) = ViewReference(*views).apply {
        blocks.add(this)
    }

    override fun delay(duration: Long) {
        blocks.add(DelayBlock(duration))
    }

    override fun delay(duration: Int) = delay(duration.toLong())

    //TODO review this
    override fun changeConstrains(root: ConstraintLayout, layoutId: Int) = ChangeConstrainsBlock(root, layoutId).apply {
        blocks.add(this)
    }

    override fun parallel(block: Orchestra.() -> Unit): Orchestra {
        val orchestraContext = Orchestra()
        block.invoke(orchestraContext)
        blocks.add(ParallelBlock(orchestraContext).also {
            lastParallelBlock = it
        })

        return orchestraContext
    }

    fun parallel(block: Consumer<Orchestra>): Orchestra {
        val orchestraContext = Orchestra()
        block.accept(orchestraContext)
        blocks.add(ParallelBlock(orchestraContext))

        return orchestraContext
    }

    override fun then(block: () -> Unit) {
        GlobalScope.launch {
            executionLatch.await()
            GlobalScope.launch(Dispatchers.Main) {
                block.invoke()
            }
        }
    }


    companion object {

        //TODO synchronize creation and disposing
        var orchestra: Orchestra? = null

        internal fun currentOrchestra(): Orchestra {
            val current = orchestra ?: createOrchestra()

            orchestra = current

            return current
        }

        internal var createOrchestra = { Orchestra() }

        internal fun disposeCurrentOrchestra() {
            orchestra?.executionLatch?.countDown()
            orchestra = null
        }

        @JvmStatic
        fun onRecycler(recyclerView: RecyclerView) = RecyclerConfiguration(recyclerView)

        fun setup(block: SetupContext.() -> Unit): SetupContext {
            val setupContext = SetupContext()
            block.invoke(setupContext)
            setupContext.runSetup()

            return setupContext
        }

        fun launch(block: OrchestraContext.() -> Unit): OrchestraContext {
            val orchestraContext = currentOrchestra()
            block.invoke(orchestraContext)
            orchestraContext.runBlocks()

            return orchestraContext
        }

        fun launch(block: Consumer<Orchestra>): Orchestra {
            val orchestraContext = Orchestra()
            block.accept(orchestraContext)
            orchestraContext.runBlocks()

            return orchestraContext
        }

        //TODO implement shot or long functionality
        fun shortOrLong(short: () -> Unit, long: () -> Unit) {
            val stacktrace = Thread.currentThread().stackTrace
            val callerClassName = stacktrace[3].className

            short()
        }

    }

}
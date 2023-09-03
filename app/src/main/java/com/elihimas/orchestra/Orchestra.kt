package com.elihimas.orchestra

import android.view.View
import com.elihimas.orchestra.blocks.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

suspend fun coroutineDelay(millis: Long) = delay(millis)

interface ParallelContext

interface OrchestraContext {
    fun changeSpeed(targetSpeed: Float)
    fun delay(duration: Long)
    fun delay(duration: Int)
    fun on(vararg views: View): ViewReference
    fun parallel(block: Orchestra.() -> Unit): ParallelContext
    fun then(block: () -> Unit)
    fun createAnimation(): AnimationsBlock
}

open class Orchestra : OrchestraContext, ParallelContext {

    private val ticker = AnimationTicker()

    private var lastParallelBlock: ParallelBlock? = null

    internal val blocks = LinkedList<Block>()
    private val executionLatch = CountDownLatch(1)

    override fun changeSpeed(targetSpeed: Float) {
        TODO("Not yet implemented")
    }

    internal fun runBlocks() {
        ticker.start(blocks)
    }

    override fun createAnimation() = AnimationsBlock()

    override fun on(vararg views: View) = ViewReference(*views).apply {
        blocks.add(this)
    }

    override fun delay(duration: Long) {
        blocks.add(DelayBlock(duration))
    }

    override fun delay(duration: Int) = delay(duration.toLong())

    override fun parallel(block: Orchestra.() -> Unit): Orchestra {
        val orchestraContext = createOrchestra()
        block.invoke(orchestraContext)
        blocks.add(ParallelBlock(orchestraContext).also {
            lastParallelBlock = it
        })

        return orchestraContext
    }

    //TODO this should be a block for two reasons:
    // if two then are called all blocks will be run together even if one of the blocks finishes first
    // executionLatch will be unnecessary
    override fun then(block: () -> Unit) {
        GlobalScope.launch {
            executionLatch.await()
            GlobalScope.launch(Dispatchers.Main) {
                block.invoke()
            }
        }
    }

    companion object {
        var orchestra: Orchestra? = null

        internal fun currentOrchestra(): Orchestra {
            val current: Orchestra
            runBlocking {
                current = orchestra ?: createOrchestra()
            }

            orchestra = current

            return current
        }

        internal var createOrchestra = { Orchestra() }

        internal fun disposeCurrentOrchestra() {
            runBlocking {
                orchestra?.executionLatch?.countDown()
                orchestra = null
            }
        }

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
    }
}
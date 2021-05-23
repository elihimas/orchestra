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
import com.elihimas.orchestra.blocks.ViewReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

suspend fun coroutineDelay(millis: Long) = delay(millis)

abstract class Block() {
    internal var start = 0F
    var end = 0F

    abstract suspend fun runBlock(orchestra: Orchestra)
    abstract fun calculateDuration(): Long

    open fun updateAnimations(time: Float) {}
    open fun updateAnimationTimeBounds() {}
}

//TODO review this class
class ChangeConstrainsBlock(private val root: ConstraintLayout, private val layoutId: Int, var duration: Long = 2600)
    : Block() {

    override fun calculateDuration() = duration

    override suspend fun runBlock(orchestra: Orchestra) {
        val transition: Transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0f)
        transition.duration = duration

        TransitionManager.beginDelayedTransition(root, transition)
        val constraintSet = ConstraintSet()
        constraintSet.clone(root.context, layoutId)

        (root.context as Activity).runOnUiThread {
            constraintSet.applyTo(root)
        }

        coroutineDelay(transition.duration)
    }

}

open class Animations : Block() {

    val currentAnimations = LinkedList<Animation>()
    var nextAnimationIndex = 0
    val animations = mutableListOf<Animation>()

    override fun calculateDuration() = animations.sumOf { animation -> animation.calculateDuration() }

    open fun <T : Animation> add(animation: T, config: (T.() -> Unit)?): Animations {
        animations.add(animation)

        config?.invoke(animation)

        return this
    }

    fun addAnimations(animations: Animations): Animations {
        this.animations.addAll(animations.animations)

        return this
    }

    fun fadeIn(config: (FadeInAnimation.() -> Unit)?) = add(FadeInAnimation(), config)
    fun fadeIn() = fadeIn(null)

    fun fadeOut(config: (FadeOutAnimation.() -> Unit)?) = add(FadeOutAnimation(1f, 0f), config)
    fun fadeOut() = fadeOut(null)

    fun scale(scaleX: Float, scaleY: Float, config: (ScaleAnimation.() -> Unit)? = null) = add(ScaleAnimation(scaleX, scaleY), config)
    fun scale(scaleX: Int, scaleY: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(scaleX.toFloat(), scaleY.toFloat(), config)
    fun scaleX(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale, 1f, config)
    fun scaleY(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) = scale(1f, scale, config)
    fun scaleX(scale: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale.toFloat(), 1f, config)
    fun scaleY(scale: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(1f, scale.toFloat(), config)
    fun scale(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale, scale, config)
    fun scale(scale: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale.toFloat(), config)

    fun slide(direction: Direction = Direction.Up, config: (SlideAnimation.() -> Unit)? = null) =
            add(SlideAnimation(direction), config)

    fun slide(direction: Direction = Direction.Up) = slide(direction, null)

    fun slideOut(direction: Direction = Direction.Up, config: (SlideOutAnimation.() -> Unit)? = null) =
            add(SlideOutAnimation(direction), config)

    fun slideOut(direction: Direction = Direction.Up) = slideOut(direction, null)

    fun circularReveal(config: (CircularRevealAnimation.() -> Unit)? = null) =
            add(CircularRevealAnimation(), config)

    fun circularReveal() = circularReveal(null)

    fun translate(x: Float, y: Float, config: (TranslateAnimation.() -> Unit)? = null) =
            add(TranslateAnimation(x, y), config)

    fun translate(x: Float, y: Float) = translate(x, y, null)

    fun changeBackground(@ColorRes vararg colorResIds: Int,
                         config: (ChangeBackgroundAnimation.() -> Unit)? = null): Animations {
        val transitionsCount = colorResIds.size - 1
        colorResIds.forEachIndexed { index, color ->
            if (index != 0) {
                val previousColor = colorResIds[index - 1]
                val animation = ChangeBackgroundAnimation(previousColor, color)
                config?.invoke(animation)

                animation.duration = animation.duration / transitionsCount

                animations.add(animation)
            }
        }

        return this
    }

    fun changeBackground(@ColorRes vararg colorResIds: Int) = changeBackground(*colorResIds, config = null)

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int,
                        config: (ChangeTextColorAnimation.() -> Unit)? = null): Animations {
        val transitionsCount = colorResIds.size - 1
        colorResIds.forEachIndexed { index, color ->
            if (index != 0) {
                val previousColor = colorResIds[index - 1]
                val animation = ChangeTextColorAnimation(previousColor, color)
                config?.invoke(animation)

                animation.duration = animation.duration / transitionsCount

                animations.add(animation)
            }
        }

        return this
    }

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int) = changeTextColor(*colorResIds, config = null)

    fun rotate(angle: Float,
               config: (RotateAnimation.() -> Unit)? = null) = add(RotateAnimation(angle), config)

    fun rotate(angle: Float) = rotate(angle, config = null)


    fun delay(duration: Long,
              config: (DelayAnimation.() -> Unit)? = null) = add(DelayAnimation(duration), config)

    fun delay(duration: Long) = delay(duration, config = null)

    fun parallel(block: Consumer<Animations>): Animations {
        Animations().also { insideReference ->
            block.accept(insideReference)
            add(ParallelAnimation(insideReference), null)//TODO: verify how to configure parallel actions
        }

        return this
    }

    fun parallel(block: Animations.() -> Unit): Animations {
        Animations().also { insideReference ->
            block.invoke(insideReference)
            val action = ParallelAnimation(insideReference)
            animations.add(action)
        }

        return this
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        TODO("Not yet implemented")
    }
}

class ParallelBlock(private val orchestraContext: Orchestra) : Block() {
    override fun updateAnimationTimeBounds() {
        orchestraContext.blocks.forEach { block ->
            block.start = start
            block.end = end

            block.updateAnimationTimeBounds()
        }

        start = orchestraContext.blocks.minOf { block -> block.start }
        end = orchestraContext.blocks.maxOf { block -> block.end }
    }

    override fun updateAnimations(time: Float) {
        orchestraContext.blocks.forEach { block ->
            block.updateAnimations(time)
        }
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        orchestraContext.blocks.let { blocks ->
            val parallelLatch = CountDownLatch(blocks.size)

            orchestraContext.blocks.forEach { block ->
                GlobalScope.launch {
                    orchestraContext.doRunBlocks(listOf(block), parallelLatch)
                }
            }

            parallelLatch.await()
        }
    }

    override fun calculateDuration() = orchestraContext.blocks.maxOf { block -> block.calculateDuration() }
}

class DelayBlock(val duration: Long) : Block() {
    override suspend fun runBlock(orchestra: Orchestra) {
        coroutineDelay(duration)
    }

    override fun calculateDuration() = duration
}

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

class Orchestra : OrchestraContext, ParallelContext {

    private val ticker = AnimationTicker()

    private var lastParallelBlock: ParallelBlock? = null

    internal val blocks = LinkedList<Block>()
    private val executionLatch = CountDownLatch(1)

    private fun runBlocks() {
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

        private fun currentOrchestra(): Orchestra {
            val current = orchestra ?: Orchestra()

            orchestra = current

            return current
        }

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


        @JvmStatic
        fun setup(block: Consumer<SetupContext>): SetupContext {
            val setupContext = SetupContext()
            block.accept(setupContext)
            setupContext.runSetup()

            return setupContext
        }

        @JvmStatic
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
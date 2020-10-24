package com.elihimas.orchestra

import android.animation.ValueAnimator
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

suspend fun coroutineDelay(millis: Long) = delay(millis)

abstract class Block() {
    var start = 0F
    var end = 0f

    abstract suspend fun runBlock(orchestra: Orchestra)
    abstract fun calculateDuration(): Long

    open fun updateAnimation(time: Float) {}
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

    val animations = mutableListOf<Animation>()

    override fun calculateDuration() = animations.sumOf { animation -> animation.duration }

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

    fun scale(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) = add(ScaleAnimation(scale), config)
    fun scale(scale: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale.toFloat(), config)
    fun scale(scale: Float) = scale(scale, null)

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
                         config: (ChangeBackgroundAnimation.() -> Unit)? = null) =
            add(ChangeBackgroundAnimation(*colorResIds), config)

    fun changeBackground(@ColorRes vararg colorResIds: Int) = changeBackground(*colorResIds, config = null)

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int,
                        config: (ChangeTextColorAnimation.() -> Unit)? = null) =
            add(ChangeTextColorAnimation(*colorResIds), config)

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int) = changeTextColor(*colorResIds, config = null)

    fun rotate(angle: Float,
               config: (RotateAnimation.() -> Unit)? = null) = add(RotateAnimation(angle), config)

    fun rotate(angle: Float) = rotate(angle, config = null)

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

open class ViewReference(private vararg val views: View) : Animations() {
    override fun updateAnimationTimeBounds() {
        var start = start
        animations.forEach { animation ->
            animation.start = start
            animation.end = start + animation.duration

            start += animation.duration
        }
    }

    override fun updateAnimation(time: Float) {
        //TODO create 'currentAnimation' structure
        views.forEach { view ->
            animations.forEach { animation ->
                val proportion = 1 - (animation.end - time) / animation.delta

                if (proportion in 0.0..1.0) {
                    animation.update(view, proportion)
                }
            }
        }
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        animations.forEach { action ->
            val latch = CountDownLatch(views.size)
            if (views.size > 1) {
                views.forEach { view ->
                    GlobalScope.launch(Dispatchers.Main) {
                        action.runAnimation(view) { latch.countDown() }
                    }

                    coroutineDelay(action.spacing)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    views.forEach { view ->
                        action.runAnimation(view) { latch.countDown() }
                    }
                }
            }
            latch.await()
        }
    }

}

class ParallelBlock(private val orchestraContext: Orchestra) : Block() {

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

class AnimationTicker {

    private var startTime = 0L

    private lateinit var blocks: MutableList<Block>

    //TODO should be a list of blocks
    private var currentBlock: Block? = null
    private var currentBlockIndex = 0

    private val updateListener: ValueAnimator.AnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        update(it.animatedValue as Float)
    }

    private fun update(time: Float) {
        currentBlock?.let { block ->
            block.updateAnimation(time)

            if (time > block.end) {
                currentBlockIndex++

                currentBlock = if (currentBlockIndex < blocks.size) {
                    blocks[currentBlockIndex]
                } else {
                    null
                }
            }
        }
    }

    fun start(blocks: MutableList<Block>) {
        var blocksDuration = 0f
        blocks.forEach { block ->
            val blockDuration = block.calculateDuration()

            block.start = blocksDuration
            block.end = block.start + blockDuration
            block.updateAnimationTimeBounds()

            blocksDuration += blockDuration
        }

        this.blocks = blocks
        ValueAnimator.ofFloat(0f, blocksDuration).apply {
            this.duration = blocksDuration.toLong()

            startTime = System.currentTimeMillis();
            currentBlock = blocks[currentBlockIndex]
            addUpdateListener(updateListener)
        }.start()
    }

}

class Orchestra : OrchestraContext, ParallelContext {

    private val ticker = AnimationTicker()

    private var lastParallelBlock: ParallelBlock? = null

    internal val blocks = mutableListOf<Block>()
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
        @JvmStatic
        fun onRecycler(recyclerView: RecyclerView) = RecyclerConfiguration(recyclerView)

        fun setup(block: SetupContext.() -> Unit): SetupContext {
            val setupContext = SetupContext()
            block.invoke(setupContext)
            setupContext.runSetup()

            return setupContext
        }

        fun launch(block: OrchestraContext.() -> Unit): OrchestraContext {
            val orchestraContext = Orchestra()
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
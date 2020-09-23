package com.elihimas.orchestra

import android.app.Activity
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewPropertyAnimator
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

interface Block {
    suspend fun runBlock(orchestra: Orchestra)
}

class FadeInAction(var initialAlpha: Float = 0f, var finalAlpha: Float = 1f) : Action(600) {

    override fun beforeAnimation(view: View) {
        view.alpha = initialAlpha
    }

    override fun addAnimation(view: View, animation: ViewPropertyAnimator) {
        animation.alpha(finalAlpha)
    }
}

typealias FadeOutAction = FadeInAction

//TODO review this class
class ChangeConstrainsBlock(private val root: ConstraintLayout, private val layoutId: Int, var duration: Long = 2600) : Block {

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

open class Animations : Block {

    val actions = mutableListOf<Action>()

    open fun <T : Action> add(action: T, config: (T.() -> Unit)?): Animations {
        actions.add(action)

        config?.invoke(action)

        return this
    }

    fun addAnimations(animations: Animations): Animations {
        actions.addAll(animations.actions)

        return this
    }

    fun fadeIn(config: (FadeInAction.() -> Unit)?) = add(FadeInAction(), config)
    fun fadeIn() = fadeIn(null)

    fun fadeOut(config: (FadeOutAction.() -> Unit)?) = add(FadeInAction(1f, 0f), config)
    fun fadeOut() = fadeOut(null)

    fun scale(scale: Float, config: (ScaleAction.() -> Unit)? = null) = add(ScaleAction(scale), config)
    fun scale(scale: Int, config: (ScaleAction.() -> Unit)? = null) = scale(scale.toFloat(), config)
    fun scale(scale: Float) = scale(scale, null)

    fun slide(direction: Direction = Direction.Up, config: (SlideAction.() -> Unit)? = null) =
            add(SlideAction(direction), config)

    fun slide(direction: Direction = Direction.Up) = slide(direction, null)

    fun slideOut(direction: Direction = Direction.Up, config: (SlideAction.() -> Unit)? = null) =
            add(SlideAction(direction, true), config)

    fun slideOut(direction: Direction = Direction.Up) = slideOut(direction, null)

    fun circularReveal(config: (CircularRevealAction.() -> Unit)? = null) =
            add(CircularRevealAction(), config)

    fun circularReveal() = circularReveal(null)

    fun translate(x: Float, y: Float, config: (TranslateAction.() -> Unit)? = null) =
            add(TranslateAction(x, y), config)

    fun translate(x: Float, y: Float) = translate(x, y, null)

    fun changeBackground(@ColorRes vararg colorResIds: Int,
                         config: (ChangeBackgroundAction.() -> Unit)? = null) =
            add(ChangeBackgroundAction(*colorResIds), config)

    fun changeBackground(@ColorRes vararg colorResIds: Int) = changeBackground(*colorResIds, config = null)

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int,
                        config: (ChangeTextColorAction.() -> Unit)? = null) =
            add(ChangeTextColorAction(*colorResIds), config)

    //TODO make available only to TextViews
    fun changeTextColor(@ColorRes vararg colorResIds: Int) = changeTextColor(*colorResIds, config = null)

    fun rotate(angle: Float,
               config: (RotateAction.() -> Unit)? = null) = add(RotateAction(angle), config)

    fun rotate(angle: Float) = rotate(angle, config = null)

    fun parallel(block: Consumer<Animations>): Animations {
        Animations().also { insideReference ->
            block.accept(insideReference)
            add(ParallelActions(insideReference), null)//TODO: verify how to configure parallel actions
        }

        return this
    }

    fun parallel(block: Animations.() -> Unit): Animations {
        Animations().also { insideReference ->
            block.invoke(insideReference)
            val action = ParallelActions(insideReference)
            actions.add(action)
        }

        return this
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        TODO("Not yet implemented")
    }
}

open class ViewReference(private vararg val views: View) : Animations() {
    override suspend fun runBlock(orchestra: Orchestra) {
        actions.forEach { action ->
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

class ParallelBlock(private val orchestraContext: Orchestra) : Block {
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
}

class DelayBlock(val duration: Long) : Block {
    override suspend fun runBlock(orchestra: Orchestra) {
        coroutineDelay(duration)
    }
}

interface ParallelContext {
}

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

    private var lastParallelBlock: ParallelBlock? = null

    internal val blocks = mutableListOf<Block>()
    private val executionLatch = CountDownLatch(1)
    private fun runBlocks() {
        GlobalScope.launch {
            doRunBlocks(blocks)
            executionLatch.countDown()
        }
    }

    internal suspend fun doRunBlocks(blocks: List<Block>, blocksLatch: CountDownLatch? = null) {
        blocks.forEach { block ->
            block.runBlock(this);
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
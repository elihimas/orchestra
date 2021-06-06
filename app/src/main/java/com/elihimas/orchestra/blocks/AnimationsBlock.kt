package com.elihimas.orchestra.blocks

import androidx.annotation.ColorRes
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.*
import java.util.*
import java.util.function.Consumer

open class AnimationsBlock : Block() {

    val currentAnimations = LinkedList<Animation>()
    var nextAnimationIndex = 0
    val animations = mutableListOf<Animation>()

    fun clone() = AnimationsBlock().also { clone ->
        this.animations.forEach { animation ->
            clone.animations.add(animation.clone() as Animation)
        }
    }

    override fun calculateDuration(): Long = animations.sumOf { animation ->
        animation.calculateDuration()
    }

    open fun <T : Animation> add(animation: T, config: (T.() -> Unit)?): AnimationsBlock {
        animations.add(animation)

        config?.invoke(animation)

        return this
    }

    fun addAnimations(animationsBlock: AnimationsBlock): AnimationsBlock {
        this.animations.addAll(animationsBlock.animations)

        return this
    }

    fun fadeIn(config: (FadeInAnimation.() -> Unit)? = null) = add(FadeInAnimation(), config)

    fun fadeOut(config: (FadeOutAnimation.() -> Unit)? = null) = add(FadeOutAnimation(1f, 0f), config)

    fun scale(scaleX: Float,
              scaleY: Float,
              config: (ScaleAnimation.() -> Unit)? = null) =
            add(ScaleAnimation(scaleX, scaleY), config)

    fun scale(scaleX: Int,
              scaleY: Int,
              config: (ScaleAnimation.() -> Unit)? = null) = scale(scaleX.toFloat(), scaleY.toFloat(), config)

    fun scale(scale: Float,
              direction: Direction,
              config: (DirectionalScaleAnimation.() -> Unit)? = null) =
            add(DirectionalScaleAnimation(scale, direction), config)

    fun scale(scale: Int,
              direction: Direction,
              config: (DirectionalScaleAnimation.() -> Unit)? = null) =
            scale(scale.toFloat(), direction, config)

    fun scaleX(scale: Float,
               config: (ScaleAnimation.() -> Unit)? = null) = scale(scale, 1f, config)

    fun scaleY(scale: Float,
               config: (ScaleAnimation.() -> Unit)? = null) = scale(1f, scale, config)

    fun scaleX(scale: Int,
               config: (ScaleAnimation.() -> Unit)? = null) = scale(scale.toFloat(), 1f, config)

    fun scaleY(scale: Int,
               config: (ScaleAnimation.() -> Unit)? = null) = scale(1f, scale.toFloat(), config)

    fun scale(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale, scale, config)
    fun scale(scale: Int, config: (ScaleAnimation.() -> Unit)? = null) = scale(scale.toFloat(), config)

    fun slide(direction: Direction = Direction.Up, config: (SlideAnimation.() -> Unit)? = null) =
            add(SlideAnimation(direction), config)


    fun slideOut(direction: Direction = Direction.Up, config: (SlideOutAnimation.() -> Unit)? = null) =
            add(SlideOutAnimation(direction), config)

    fun circularReveal(config: (CircularRevealAnimation.() -> Unit)? = null) =
            add(CircularRevealAnimation(), config)


    fun translate(x: Float, y: Float, config: (TranslateAnimation.() -> Unit)? = null) =
            add(TranslateAnimation(x, y), config)

    fun changeBackground(@ColorRes vararg colorResIds: Int,
                         config: (ChangeBackgroundAnimation.() -> Unit)? = null): AnimationsBlock {
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
                        config: (ChangeTextColorAnimation.() -> Unit)? = null): AnimationsBlock {
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

    // TODO: create a data structure to hold the repeat times and store the animations
    // the current implementation is just a facilitator
    fun repeat(times: Int, animationsBlock: AnimationsBlock): AnimationsBlock {
        for (index in 0..times) {
            addAnimations(animationsBlock.clone())
        }

        return this
    }

    fun parallel(block: Consumer<AnimationsBlock>): AnimationsBlock {
        AnimationsBlock().also { insideReference ->
            block.accept(insideReference)
            add(ParallelAnimation(insideReference), null)//TODO: verify how to configure parallel actions
        }

        return this
    }

    fun parallel(block: AnimationsBlock.() -> Unit): AnimationsBlock {
        AnimationsBlock().also { animations ->
            block.invoke(animations)
            val parallelAnimation = ParallelAnimation(animations)
            this.animations.add(parallelAnimation)
        }

        return this
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        TODO("Not yet implemented")
    }
}
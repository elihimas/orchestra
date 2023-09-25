package com.elihimas.orchestra.blocks

import androidx.annotation.ColorRes
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.animations.CircularRevealAnimation
import com.elihimas.orchestra.animations.DelayAnimation
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.animations.opacity.FadeInAnimation
import com.elihimas.orchestra.animations.opacity.FadeOutAnimation
import com.elihimas.orchestra.animations.ParallelAnimation
import com.elihimas.orchestra.animations.rotation.RotateByAnimation
import com.elihimas.orchestra.animations.rotation.RotateToAnimation
import com.elihimas.orchestra.animations.colors.ChangeBackgroundAnimation
import com.elihimas.orchestra.animations.colors.ChangeTextColorAnimation
import com.elihimas.orchestra.animations.opacity.OpacityToAnimation
import com.elihimas.orchestra.animations.scale.DirectionalScaleAnimation
import com.elihimas.orchestra.animations.scale.ScaleAnimation
import com.elihimas.orchestra.animations.slide.SlideAnimation
import com.elihimas.orchestra.animations.slide.SlideOutAnimation
import com.elihimas.orchestra.animations.translations.ResetTranslationAnimation
import com.elihimas.orchestra.animations.translations.TranslateByAnimation
import com.elihimas.orchestra.animations.translations.TranslateToPositionAnimation
import com.elihimas.orchestra.animations.translations.TranslateToReferenceAnimation
import com.elihimas.orchestra.references.TranslationReference
import java.util.LinkedList
import java.util.function.Consumer

open class AnimationsBlock : Block() {
    // TODO add x(Consumer<XAnimation>) versions for all functions to better support Java

    internal val currentAnimations = LinkedList<Animation>()
    internal var nextAnimationIndex = 0
    internal val animations = mutableListOf<Animation>()

    override fun resetForeverData() {
        // TODO: review this
    }

    fun clone() = AnimationsBlock().also { clone ->
        this.animations.forEach { animation ->
            clone.animations.add(animation.clone() as Animation)
        }
    }

    override fun checkHasForeverAnimation(): Boolean {
        // TODO: review this
        return animations.any { it.isInfinite }
    }

    override fun calculateDuration(): Long = animations.sumOf { animation ->
        animation.calculateDuration(viewsCount)
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

    fun opacityTo(targetAlpha: Float, config: ((OpacityToAnimation.() -> Unit))? = null) =
        add(OpacityToAnimation(targetAlpha), config)

    fun fadeIn() = add(FadeInAnimation(), null)

    fun fadeIn(config: (FadeInAnimation.() -> Unit)) = add(FadeInAnimation(), config)

    fun fadeIn(config: Consumer<FadeInAnimation>) =
        add(FadeInAnimation(), config::accept)

    fun fadeOut(config: (FadeOutAnimation.() -> Unit)? = null) =
        add(FadeOutAnimation(), config)

    fun fadeOut(config: Consumer<FadeOutAnimation>) =
        add(FadeOutAnimation(), config::accept)

    fun scale(
        scaleX: Float = 1f,
        scaleY: Float = 1f,
        config: (ScaleAnimation.() -> Unit)? = null
    ) =
        add(ScaleAnimation(scaleX, scaleY), config)

    fun scale(
        scaleX: Int,
        scaleY: Int,
        config: (ScaleAnimation.() -> Unit)? = null
    ) = scale(scaleX.toFloat(), scaleY.toFloat(), config)

    fun scale(
        scale: Float,
        direction: Direction,
        config: (DirectionalScaleAnimation.() -> Unit)? = null
    ) =
        add(DirectionalScaleAnimation(scale, direction), config)

    fun scaleY(
        scale: Float,
        config: (ScaleAnimation.() -> Unit)? = null
    ) = scale(1f, scale, config)

    fun scaleY(
        scale: Int,
        config: (ScaleAnimation.() -> Unit)? = null
    ) = scale(1f, scale.toFloat(), config)

    fun scale(scale: Float, config: (ScaleAnimation.() -> Unit)? = null) =
        scale(scale, scale, config)

    fun scale(scale: Float, config: Consumer<ScaleAnimation>) =
        scale(scale, config::accept)

    fun scaleY(scale: Float, config: Consumer<ScaleAnimation>) =
        scale(1f, scale) { config.accept(this) }

    fun scale(scaleX: Float, scaleY: Float, config: Consumer<ScaleAnimation>) =
        scale(scaleX, scaleY) { config.accept(this) }

    fun slideIn(direction: Direction = Direction.Up, config: (SlideAnimation.() -> Unit)? = null) =
        add(SlideAnimation(direction), config)

    fun slideOut(
        direction: Direction = Direction.Up,
        config: (SlideOutAnimation.() -> Unit)? = null
    ) =
        add(SlideOutAnimation(direction), config)

    fun circularReveal(config: (CircularRevealAnimation.() -> Unit)? = null) =
        add(CircularRevealAnimation(), config)


    fun translateBy(x: Float, y: Float, config: (TranslateByAnimation.() -> Unit)? = null) =
        add(TranslateByAnimation(x, y), config)

    fun translateTo(x: Float, y: Float, config: (TranslateToPositionAnimation.() -> Unit)? = null) =
        add(TranslateToPositionAnimation(x, y), config)

    fun translateTo(
        reference: TranslationReference,
        config: (TranslateToReferenceAnimation.() -> Unit)? = null
    ) =
        add(TranslateToReferenceAnimation(reference), config)

    fun resetTranslation(
        config: (ResetTranslationAnimation.() -> Unit)? = null
    ) =
        add(ResetTranslationAnimation(), config)

    fun changeBackground(
        @ColorRes vararg colorResIds: Int,
        config: (ChangeBackgroundAnimation.() -> Unit)? = null
    ): AnimationsBlock {
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

    fun changeBackground(@ColorRes vararg colorResIds: Int) =
        changeBackground(*colorResIds, config = null)

    //TODO make available only to TextViews
    fun changeTextColor(
        @ColorRes vararg colorResIds: Int,
        config: (ChangeTextColorAnimation.() -> Unit)? = null
    ): AnimationsBlock {
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
    fun changeTextColor(@ColorRes vararg colorResIds: Int) =
        changeTextColor(*colorResIds, config = null)

    fun rotateBy(
        angle: Float,
        config: (RotateByAnimation.() -> Unit)? = null
    ) = add(RotateByAnimation(angle), config)

    fun rotateTo(
        angle: Float,
        config: (RotateToAnimation.() -> Unit)? = null
    ) = add(RotateToAnimation(angle), config)


    fun delay(
        duration: Long,
        config: (DelayAnimation.() -> Unit)? = null
    ) = add(DelayAnimation(duration), config)

    fun delay(duration: Long) = delay(duration, config = null)

    // TODO: create a data structure to hold the repeat times and store the animations
    // the current implementation is just a facilitator
    fun repeat(times: Int, animationsBlock: AnimationsBlock): AnimationsBlock {
        for (index in 1..times) {
            addAnimations(animationsBlock.clone())
        }

        return this
    }

    fun parallel(block: Consumer<AnimationsBlock>): AnimationsBlock {
        AnimationsBlock().also { insideReference ->
            block.accept(insideReference)
            add(
                ParallelAnimation(insideReference),
                null
            )//TODO: verify how to configure parallel actions
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

    // TODO: review this
    open fun forever(block: AnimationsBlock.() -> Unit) {

    }

    // TODO: review this
    open fun forever(animationsBlock: AnimationsBlock) {

    }
}
package com.elihimas.orchestra.ui.animateimage.usecases

import android.widget.ImageView
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.*
import com.elihimas.orchestra.blocks.ViewReference

//TODO verify if view must be a weak reference
class AnimateUseCase(private val imageView: ImageView) {

    fun execute(animations: MutableList<Animation>?) {
        Orchestra.launch {
            on(imageView).apply {
                animations?.forEach { animation ->
                    when (animation) {
                        is TranslateAnimation -> addTranslate(animation)
                        is FadeInAnimation -> addFadeIn(animation)
                        is FadeOutAnimation -> addFadeOut(animation)
                        is ScaleAnimation -> addScale(animation)
                        is SlideAnimation -> addSlide(animation)
                        is SlideOutAnimation -> addSlideOut(animation)
                        is RotateAnimation -> addRotate(animation)
                        is CircularRevealAnimation -> addCircularReveal(animation)
                    }
                }
            }
        }
    }

    private fun ViewReference.addTranslate(animation: TranslateAnimation) {
        translate(animation.x, animation.y) {
            duration = animation.duration
            spacing = animation.spacing
        }
    }

    private fun ViewReference.addFadeIn(animation: FadeInAnimation) {
        fadeIn {
            duration = animation.duration
            spacing = animation.spacing
            initialAlpha = animation.initialAlpha
            finalAlpha = animation.finalAlpha
        }
    }

    private fun ViewReference.addFadeOut(animation: FadeOutAnimation) {
        fadeOut {
            duration = animation.duration
            spacing = animation.spacing
            initialAlpha = animation.initialAlpha
            finalAlpha = animation.finalAlpha
        }
    }

    private fun ViewReference.addScale(animation: ScaleAnimation) {
        scale(animation.scaleX, animation.scaleY) {
            duration = animation.duration
            spacing = animation.spacing
        }
    }

    private fun ViewReference.addSlide(animation: SlideAnimation) {
        slide {
            duration = animation.duration
            spacing = animation.spacing
            direction = animation.direction
        }
    }

    private fun ViewReference.addSlideOut(animation: SlideOutAnimation) {
        slideOut {
            duration = animation.duration
            spacing = animation.spacing
            direction = animation.direction
        }
    }

    private fun ViewReference.addRotate(animation: RotateAnimation) {
        rotate(animation.angle) {
            duration = animation.duration
            spacing = animation.spacing
        }
    }

    private fun ViewReference.addCircularReveal(animation: Animation) {
        circularReveal {
            duration = animation.duration
            spacing = animation.spacing
        }
    }

}

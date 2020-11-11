package com.elihimas.orchestra.ui.animateimage.usecases

import com.elihimas.orchestra.animations.*
import com.elihimas.orchestra.model.AnimateImageAnimations

class EnumToAnimationUseCase {

    fun execute(animationEnum: AnimateImageAnimations): Animation =
            when (animationEnum) {
                AnimateImageAnimations.FadeIn -> FadeInAnimation()
                AnimateImageAnimations.FadeOut -> FadeOutAnimation()
                AnimateImageAnimations.Translate -> TranslateAnimation(10f, 10f)
                AnimateImageAnimations.Scale -> ScaleAnimation(2f)
                AnimateImageAnimations.Slide -> SlideAnimation(Direction.Right)
                AnimateImageAnimations.SlideOut -> SlideAnimation(Direction.Right, reverseAnimation = true)
                AnimateImageAnimations.CircularReveal -> CircularRevealAnimation()
                AnimateImageAnimations.Rotate -> RotateAnimation(90f)
            }

}

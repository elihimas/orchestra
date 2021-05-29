package com.elihimas.orchestra.model

import com.elihimas.orchestra.R

object ResourcesMapper {

    fun map(example: Examples) = when (example) {
        Examples.Interpolator -> R.string.example_interpolator
        Examples.AnimateImage -> R.string.animate_image
        Examples.Bouncing -> R.string.bouncing
        Examples.BackgroundAndTextColor -> R.string.background_text_color
        Examples.ConstrainsLayout -> R.string.constrains_layout
        Examples.CoordinatorLayout -> R.string.coordinator_layout
        Examples.Form -> R.string.form
    }

    fun map(animateImageAnimations: AnimateImageAnimations) = when (animateImageAnimations) {
        AnimateImageAnimations.FadeIn -> R.string.fade_in
        AnimateImageAnimations.FadeOut -> R.string.fade_out
        AnimateImageAnimations.Translate -> R.string.translate
        AnimateImageAnimations.Scale -> R.string.scale
        AnimateImageAnimations.Slide -> R.string.slide
        AnimateImageAnimations.SlideOut -> R.string.slide_out
        AnimateImageAnimations.Rotate -> R.string.rotate
        AnimateImageAnimations.CircularReveal -> R.string.circular_reveal
    }

}

package com.elihimas.orchestra.model

import com.elihimas.orchestra.R

object ResourcesMapper {

    fun map(example: Examples) = when (example) {
        Examples.Splash -> R.string.splash
        Examples.Fade -> R.string.example_fade
        Examples.Scale -> R.string.example_scale
        Examples.Extensions -> R.string.example_extensions
        Examples.Interpolator -> R.string.example_interpolator
        Examples.AnimateImage -> R.string.animate_image
        Examples.Bouncing -> R.string.example_bouncing
        Examples.BackgroundAndTextColor -> R.string.example_background_text_color
        Examples.ConstrainsLayout -> R.string.example_constrains_layout
        Examples.CoordinatorLayout -> R.string.example_coordinator_layout
        Examples.Slide -> R.string.example_slide
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
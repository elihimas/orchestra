package com.elihimas.orchestra.model

import com.elihimas.orchestra.R

object ResourcesMapper {
    fun mapExampleName(example: Examples) = when (example) {
        Examples.FadeIn -> R.string.fade_in
        Examples.FadeOut -> R.string.fade_out
        Examples.Translate -> R.string.translate
        Examples.Scale -> R.string.scale
        Examples.Slide -> R.string.slide
        Examples.SlideOut -> R.string.slide_out
        Examples.Bouncing -> R.string.bouncing
        Examples.CircularReveal -> R.string.circular_reveal
        Examples.ConstrainsLayout -> R.string.constrains_layout
        Examples.CoordinatorLayout -> R.string.coordinator_layout
        Examples.Form -> R.string.form
    }

}

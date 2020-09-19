package com.elihimas.orchestra.usecases

import android.widget.ImageView
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.model.Examples
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

class ButterflyImageConfigurator(private val butterflyImage: ImageView) {

    fun execute(example: Examples) =
            Orchestra.setup {
                val setupReference = on(butterflyImage)

                when (example) {
                    Examples.SlideOut, Examples.ConstrainsLayout, Examples.Form,
                    Examples.Translate, Examples.Scale -> {
                        //nothing to do
                    }
                    Examples.CircularReveal, Examples.Slide -> {
                        setupReference.invisible()
                    }
                    Examples.FadeIn -> {
                        setupReference.alpha(0f)
                    }
                    Examples.FadeOut -> {
                        setupReference.alpha(1f)
                    }
                    Examples.CoordinatorLayout -> {
                        setupReference
                    }
                }
            }
}

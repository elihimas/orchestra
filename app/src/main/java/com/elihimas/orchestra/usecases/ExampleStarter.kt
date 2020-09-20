package com.elihimas.orchestra.usecases

import android.content.Context
import android.content.Intent
import com.elihimas.orchestra.activities.*
import com.elihimas.orchestra.model.Examples

class ExampleStarter(private val context: Context) {

    fun execute(example: Examples) {
        val nextClass = when (example) {
            Examples.FadeIn, Examples.FadeOut, Examples.Scale,
            Examples.Slide, Examples.SlideOut, Examples.Translate,
            Examples.CircularReveal -> null

            Examples.Bouncing -> BouncingButtonExampleActivity::class.java
            Examples.CoordinatorLayout -> CoordinatorExampleActivity::class.java
            Examples.ConstrainsLayout -> ConstrainsExampleActivity::class.java
            Examples.Form -> FormExampleActivity::class.java

        }
        if (nextClass == null) {
            CenteredImageActivity.startActivity(context, example)
        } else {
            context.startActivity(Intent(context, nextClass))
        }
    }
}
package com.elihimas.orchestra.usecases

import android.content.Context
import android.content.Intent
import com.elihimas.orchestra.activities.*
import com.elihimas.orchestra.model.Examples
import com.elihimas.orchestra.ui.InterpolatorActivity
import com.elihimas.orchestra.ui.animateimage.AnimateImageActivity

class ExampleStarter(private val context: Context) {

    fun execute(example: Examples) {
        val nextClass = when (example) {
            Examples.Interpolator -> InterpolatorActivity::class.java
            Examples.AnimateImage -> AnimateImageActivity::class.java
            Examples.Bouncing -> BouncingButtonExampleActivity::class.java
            Examples.BackgroundAndTextColor -> BackgroundAndTextColorExampleActivity::class.java
            Examples.CoordinatorLayout -> CoordinatorExampleActivity::class.java
            Examples.ConstrainsLayout -> ConstrainsExampleActivity::class.java
            Examples.Form -> FormExampleActivity::class.java
        }

        context.startActivity(Intent(context, nextClass))
    }
}
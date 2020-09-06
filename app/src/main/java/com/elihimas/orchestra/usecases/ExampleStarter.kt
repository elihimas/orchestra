package com.elihimas.orchestra.usecases

import android.content.Context
import android.content.Intent
import com.elihimas.orchestra.activities.*
import com.elihimas.orchestra.model.Examples

class ExampleStarter(private val context: Context) {

    fun start(example: Examples) {
        val nextClass = when (example) {
            Examples.FadeIn -> FadeInExampleActivity::class.java
            Examples.FadeOut -> FadeOutExampleActivity::class.java
            Examples.Translate -> TranslateExampleActivity::class.java
            Examples.Scale -> ScaleExampleActivity::class.java
            Examples.CoordinatorLayout -> CoordinatorExampleActivity::class.java
            Examples.Form -> FormExampleActivity::class.java
        }
        context.startActivity(Intent(context, nextClass))
    }
}
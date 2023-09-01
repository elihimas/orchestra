package com.elihimas.orchestra.showcase.examples

import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.activities.AnimatedButtonActivity
import com.elihimas.orchestra.showcase.activities.ScaleActivity
import com.elihimas.orchestra.showcase.activities.ScalingLoginActivity
import com.elihimas.orchestra.showcase.activities.SlidingLoginActivity
import com.elihimas.orchestra.showcase.activities.TranslateActivity

class Example(val clazz: Class<*>, val exampleStringId: Int) {

    ////    Splash,
//    Scale,
//    Fade,
//    Extensions,
//    Interpolator,
////    AnimateImage,
//    Bouncing,
//    BackgroundAndTextColor,
//    ConstrainsLayout,
////    CoordinatorLayout,
//    Slide,
    companion object {
        fun getExamples() = listOf(
            Example(SlidingLoginActivity::class.java, R.string.example_sliding_login),
            Example(ScalingLoginActivity::class.java, R.string.example_scaling_login),
            Example(AnimatedButtonActivity::class.java, R.string.example_animated_button),
            Example(TranslateActivity::class.java, R.string.example_translation),
            Example(ScaleActivity::class.java, R.string.example_scaling_login),
        )
    }
}
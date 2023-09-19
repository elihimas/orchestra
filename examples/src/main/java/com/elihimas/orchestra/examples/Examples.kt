package com.elihimas.orchestra.examples

import com.elihimas.orchestra.adapter.Example
import com.elihimas.orchestra.examples.activities.FadeActivity
import com.elihimas.orchestra.examples.activities.ScalingActivity
import com.elihimas.orchestra.examples.activities.SlideActivity
import com.elihimas.orchestra.examples.activities.SpacingActivity
import com.elihimas.orchestra.examples.activities.TranslateActivity

object Examples {
    fun getExamples() = listOf(
        Example(FadeActivity::class.java, R.string.example_fade),
        Example(SlideActivity::class.java, R.string.example_slide),
        Example(TranslateActivity::class.java, R.string.example_translate),
        Example(SpacingActivity::class.java, R.string.example_spacing),
        Example(ScalingActivity::class.java, R.string.example_scaling),
    )

}

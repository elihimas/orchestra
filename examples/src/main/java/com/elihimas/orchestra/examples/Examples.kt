package com.elihimas.orchestra.examples

import com.elihimas.orchestra.adapter.Example
import com.elihimas.orchestra.examples.activities.FadeActivity
import com.elihimas.orchestra.examples.activities.SlideActivity

object Examples {
    fun getExamples() = listOf(
        Example(FadeActivity::class.java, R.string.example_fade),
        Example(SlideActivity::class.java, R.string.example_slide),
    )

}

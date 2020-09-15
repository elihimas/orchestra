package com.elihimas.orchestra

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_config_orchestra.view.*

class ConfigOrchestraView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        inflate(context, R.layout.view_config_orchestra, this)
    }

    val duration: Int
        get() = durationSeekBar.progress
    val initialAlpha: Int
        get() = initialAlphaSeekBar.progress
    val finalAlpha: Int
        get() = finalAlphaSeekBar.progress

    fun onAnimate(animateFunction: () -> Unit) {
        animateButton.setOnClickListener {
            animateButton.isEnabled = false
            animateFunction()
        }
    }

    fun enableAnimateButton() {
        animateButton.isEnabled = true
    }

}
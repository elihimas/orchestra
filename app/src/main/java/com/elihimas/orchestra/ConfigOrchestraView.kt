package com.elihimas.orchestra

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_config_orchestra.view.*

class ConfigOrchestraView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        inflate(context, R.layout.view_config_orchestra, this)
    }

    var alphaControls: Boolean
        get() = initialAlphaSeekBar.visibility == View.VISIBLE
        set(value) {
            val visibility = if (value) {
                View.VISIBLE
            } else {
                View.GONE
            }
            visibility.let {
                initialAlphaText.visibility = it
                initialAlphaSeekBar.visibility = it

                finalAlphaText.visibility = it
                finalAlphaSeekBar.visibility = it
            }
        }
    val duration: Int
        get() = durationSeekBar.progress
    var initialAlpha: Float
        get() = (initialAlphaSeekBar.progress / 100).toFloat()
        set(value) {
            initialAlphaSeekBar.progress = (value * 100).toInt()
        }
    var finalAlpha: Float
        get() = (finalAlphaSeekBar.progress / 100).toFloat()
        set(value) {
            finalAlphaSeekBar.progress = (value * 100).toInt()
        }

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
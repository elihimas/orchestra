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

    var showScaleControls: Boolean
        get() = scaleSeekBar.visibility == View.VISIBLE
        set(value) {
            val visibility = if (value) {
                View.VISIBLE
            } else {
                View.GONE
            }
            visibility.let {
                scaleText.visibility = it
                scaleSeekBar.visibility = it
            }
        }
    var showAlphaControls: Boolean
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

    val duration: Long
        get() = durationSeekBar.progress.toLong()
    val scale: Float
        get() = scaleSeekBar.progress.toFloat() / 10
    var initialAlpha: Float
        get() = initialAlphaSeekBar.progress.toFloat() / 100
        set(value) {
            initialAlphaSeekBar.progress = (value * 100).toInt()
        }
    var finalAlpha: Float
        get() = finalAlphaSeekBar.progress.toFloat() / 100
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
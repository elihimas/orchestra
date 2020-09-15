package com.elihimas.orchestra

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.elihimas.orchestra.usecases.AnimationConfiguration
import kotlinx.android.synthetic.main.view_config_orchestra.view.*

class ConfigOrchestraView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), AnimationConfiguration {

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

    override var duration: Long
        get() = durationSeekBar.progress.toLong()
        set(value) {
            durationSeekBar.progress = (value).toInt()
        }
    override var scale: Float
        get() = scaleSeekBar.progress.toFloat() / 10
        set(value) {
            scaleSeekBar.progress = (value * 10).toInt()
        }
    override var initialAlpha: Float
        get() = initialAlphaSeekBar.progress.toFloat() / 100
        set(value) {
            initialAlphaSeekBar.progress = (value * 100).toInt()
        }
    override var finalAlpha: Float
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
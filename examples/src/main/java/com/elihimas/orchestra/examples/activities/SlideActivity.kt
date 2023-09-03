package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivitySlideBinding

class SlideActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySlideBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        btSlideIn.setOnClickListener { onSlideInClicked() }
        btSlideOut.setOnClickListener { onSlideOutClicked() }
    }

    private fun onSlideInClicked() = with(binding) {
        val direction = directionSelector.getSelectedDirection()
        val interpolator = interpolatorSelector.getSelectedInterpolator()

        Orchestra.launch {
            on(slideTarget).slideIn(direction) {
                this.duration = 2_000
                this.remainingWidth = getRemainingWidth()
                this.interpolator = interpolator
            }
        }
    }

    private fun getRemainingWidth(): Float {
        return binding.etRemaining.text.toString().toFloatOrNull() ?: 0f
    }

    private fun onSlideOutClicked() = with(binding) {
        val direction = directionSelector.getSelectedDirection()
        val interpolator = interpolatorSelector.getSelectedInterpolator()

        Orchestra.launch {
            on(slideTarget).slideOut(direction) {
                duration = 2_000
                remainingWidth = getRemainingWidth()
                this.interpolator = interpolator
            }
        }
    }


}
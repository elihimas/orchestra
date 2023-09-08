package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivitySlideBinding
import com.elihimas.orchestra.examples.databinding.ExampleTargetBinding

class SlideActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySlideBinding.inflate(layoutInflater) }
    private val exampleTargetBinding by lazy { ExampleTargetBinding.bind(binding.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()

        binding.root.post {
            onUpdateStatsClicked()
        }
    }

    private fun initViews() = with(binding) {
        tvStats.setOnClickListener { onUpdateStatsClicked() }

        btReset.setOnClickListener { onResetClicked() }
        btSlideIn.setOnClickListener { onSlideInClicked() }
        btSlideOut.setOnClickListener { onSlideOutClicked() }

        exampleTargetBinding.targetShadow.isVisible = true
    }

    private fun onUpdateStatsClicked() {
        val status = createStats()
        binding.tvStats.text = status
    }

    private fun createStats(): String = with(exampleTargetBinding.exampleTarget) {
        return "width: $width\n" +
                "height: $height\n" +
                "translationX: $translationX\n" +
                "translationY: $translationY\n" +
                if (clipBounds != null) {
                    "clipBounds: L:${clipBounds.left} R:${clipBounds.right} T:${clipBounds.top} B:${clipBounds.bottom}"
                } else {
                    ""
                }
    }

    private fun onResetClicked() {
        with(exampleTargetBinding.exampleTarget) {
            translationX = 0f
            translationY = 0f
            visibility = View.VISIBLE
            clipBounds = null
        }
    }

    private fun onSlideInClicked() = with(binding) {
        val direction = directionSelector.getSelectedDirection()
        val interpolator = interpolatorSelector.getSelectedInterpolator()

        Orchestra.launch {
            on(exampleTargetBinding.exampleTarget).slideIn(direction) {
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
            on(exampleTargetBinding.exampleTarget).slideOut(direction) {
                duration = 2_000
                remainingWidth = getRemainingWidth()
                this.interpolator = interpolator
            }
        }
    }


}
package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivitySpacingBinding
import com.elihimas.orchestra.references.horizontalCenterOf

class SpacingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySpacingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener { runAnimations() }
    }

    private fun runAnimations() {
        with(binding) {
            listOf(scalingSqr1, scalingSqr2, scalingSqr3, scalingSqr4).forEach { sqr ->
                sqr.scaleX = 1f
                sqr.scaleY = 1f
            }

            Orchestra.launch {
                on(scalingSqr1, scalingSqr2, scalingSqr3, scalingSqr4).scale(2f) {
                    spacing = 200
                    duration = 700
                }

                on(
                    translatingSqr4,
                    translatingSqr3,
                    translatingSqr2,
                    translatingSqr1,
                ).translateTo(
                    horizontalCenterOf(rightGuide)
                ) {
                    duration = 2_000
                    spacing = 800
                    interpolator = AccelerateDecelerateInterpolator()
                }
            }
        }
    }
}
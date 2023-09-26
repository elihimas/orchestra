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

            listOf(
                translatingSqr1,
                translatingSqr2,
                translatingSqr3,
                translatingSqr4
            ).forEach { view ->
                view.translationX = 0f
                view.translationY = 0f
            }

            listOf(fadeOut1, fadeOut2, fadeOut3, fadeOut4).forEach { view ->
                view.alpha = 1f
            }

            Orchestra.launch {
                parallel {
                    on(scalingSqr1, scalingSqr2, scalingSqr3, scalingSqr4).scale(2f) {
                        duration = 700
                        timeSpacing = 200
                    }

                    on(
                        translatingSqr4,
                        translatingSqr3,
                        translatingSqr2,
                        translatingSqr1,
                    )
                        .translateTo(
                            horizontalCenterOf(rightGuide)
                        ) {
                            duration = 1000
                            timeSpacing = 200
                            interpolator = AccelerateDecelerateInterpolator()
                        }

                    on(fadeOut1, fadeOut2, fadeOut3, fadeOut4)
                        .fadeOut {
                            duration = 1_800
                            timeSpacing = 600
                        }
                }
            }
        }
    }
}
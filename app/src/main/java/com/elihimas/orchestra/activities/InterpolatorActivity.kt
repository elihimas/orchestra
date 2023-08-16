package com.elihimas.orchestra.activities

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.databinding.ActivityInterpolatorBinding

class InterpolatorActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInterpolatorBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() = with(binding) {
        btRun.setOnClickListener { runAnimations(slowly = false) }
        btRunSlowly.setOnClickListener { runAnimations(slowly = true) }
        setupSquaresClicks()

        runAnimations(slowly = false)
    }

    private fun setupSquaresClicks() = with(binding) {
        val viewsAndInterpolators = listOf(
            Pair(square1, AccelerateDecelerateInterpolator()),
            Pair(square2, AccelerateInterpolator()),
            Pair(square3, AnticipateInterpolator()),
            Pair(square4, BounceInterpolator()),
            Pair(square5, FastOutLinearInInterpolator()),
            Pair(square6, OvershootInterpolator())
        )

        viewsAndInterpolators.forEach { viewAndInterpolator ->
            viewAndInterpolator.first.setOnClickListener {
                Orchestra.launch {
                    runAnimation(this, viewAndInterpolator.first, viewAndInterpolator.second)
                }
            }
        }
    }

    private fun runAnimations(slowly: Boolean) = with(binding) {
        val viewsAndInterpolators = listOf(
            Pair(square1, AccelerateDecelerateInterpolator()),
            Pair(square2, AccelerateInterpolator()),
            Pair(square3, AnticipateInterpolator()),
            Pair(square4, BounceInterpolator()),
            Pair(square5, FastOutLinearInInterpolator()),
            Pair(square6, OvershootInterpolator())
        )

        val durationMultiplier = if (slowly) {
            4
        } else {
            1
        }

        viewsAndInterpolators.forEach { viewAndInterpolator ->
            val view = viewAndInterpolator.first
            val interpolator = viewAndInterpolator.second

            Orchestra.launch {
                runAnimation(this, view, interpolator, durationMultiplier)
            }
        }
    }

    private fun runAnimation(
        orchestraContext: OrchestraContext,
        view: View,
        interpolator: Interpolator,
        durationMultiplier: Int = 1
    ) {
        orchestraContext.parallel {
            on(view)
                .rotate(180.0f) {
                    this.interpolator = interpolator
                    duration = 2400L * durationMultiplier
                }
                .scale(2) {
                    this.interpolator = interpolator
                    duration = 2000L * durationMultiplier
                }
                .scale(1) {
                    this.interpolator = interpolator
                    duration = 2000L * durationMultiplier
                }
        }
    }
}
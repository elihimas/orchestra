package com.elihimas.orchestra.showcase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.databinding.ActivityTranslateBinding

class TranslateActivityData(
    var isSettingsVisible: Boolean = false,
    var showConstrainedObject: Boolean = true,
    var interpolator: Interpolator = LinearInterpolator()
)

class TranslateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTranslateBinding.inflate(layoutInflater) }

    private val data = TranslateActivityData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initOrchestra()
    }

    private fun initViews() = with(binding) {
        root.setOnTouchListener { _, event ->
            val x = event.x
            val y = event.y

            translateTo(x, y)
            true
        }

        btUp.setOnClickListener {
            translateUp()
        }
        btDown.setOnClickListener {
            translateDown()
        }
        btLeft.setOnClickListener {
            translateLeft()
        }
        btRight.setOnClickListener {
            translateRight()
        }

        btSettings.setOnClickListener {
            toggleSettings()
        }

        btApply.setOnClickListener {
            onApplyClicked()
        }
    }

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(constrainedView).follows(targetView)
        }

        Orchestra.launch {
            on(tvHint)
                .delay(2_000)
                .fadeIn()
                .delay(5_000)
                .fadeOut()
        }
    }

    private fun onApplyClicked() {
        toggleSettings().andThen {
            updateShowConstrainedElement()
            updateInterpolator()
        }
    }

    private fun updateShowConstrainedElement() = with(binding) {
        val newShowConstrainedElement = cbShowConstrainedElement.isChecked

        if ((newShowConstrainedElement != data.showConstrainedObject)) {
            if (newShowConstrainedElement) {
                Orchestra.launch {
                    on(constrainedView).fadeIn()
                }
            } else {
                Orchestra.launch {
                    on(constrainedView).fadeOut()
                }
            }
        }

        data.showConstrainedObject = newShowConstrainedElement
    }

    private fun updateInterpolator() = with(binding) {
        data.interpolator = if (rbLinear.isChecked) {
            LinearInterpolator()
        } else if (rbAccelerate.isChecked) {
            AccelerateInterpolator()
        } else if (rbBounce.isChecked) {
            BounceInterpolator()
        } else if (rbAnticipate.isChecked) {
            AnticipateInterpolator()
        } else if (rbOvershoot.isChecked) {
            OvershootInterpolator()
        } else {
            AnticipateOvershootInterpolator()
        }
    }

    private fun translateUp() {
        translate(0f, -200f)
    }

    private fun translateDown() {
        translate(0f, 200f)
    }

    private fun translateLeft() {
        translate(-200f, 0f)
    }

    private fun translateRight() {
        translate(200f, 0f)
    }

    private fun translate(x: Float, y: Float) {
        Orchestra.launch {
            on(binding.targetView).translateBy(x, y) {
                interpolator = data.interpolator
                duration = 1000
            }
        }
    }

    private fun translateTo(x: Float, y: Float) {
        Orchestra.launch {
            on(binding.targetView).translateTo(x, y) {
                interpolator = data.interpolator
                duration = 900

                areCoordinatesCenter = true
            }
        }
    }

    private fun toggleSettings(): OrchestraContext {
        return if (data.isSettingsVisible) {
            hideSettings()
        } else {
            showSettings()
        }.also {
            data.isSettingsVisible = !data.isSettingsVisible
        }
    }

    private fun hideSettings(): OrchestraContext {
        return Orchestra.launch {
            on(binding.settingsContainer).slideOut(Direction.Right) {
                duration = 300
            }
        }
    }

    private fun showSettings(): OrchestraContext {
        binding.settingsContainer.visibility = android.view.View.VISIBLE

        return Orchestra.launch {

            on(binding.settingsContainer)
                .slideIn(Direction.Left) {
                    duration = 300
                }
        }
    }
}
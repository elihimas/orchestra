package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.examples.databinding.ActivityTranslateBinding
import com.elihimas.orchestra.references.leftOf
import java.lang.IllegalStateException

class TranslateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTranslateBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()

        updateStatus()
    }

    private fun initViews() = with(binding) {
        tvCenterReference.setOnClickListener { onCenterClicked() }

        tvStatus.setOnClickListener { updateStatus() }

        btReset.setOnClickListener { onResetClicked() }
        btTranslateBy.setOnClickListener { onTranslateByClicked() }
        btTranslateToPosition.setOnClickListener { onTranslateToClicked() }
        btTranslateToReference.setOnClickListener { onTranslateToReferenceClicked() }
    }

    private fun updateStatus() {
        val newStatus = createStatus()
        binding.tvStatus.text = newStatus
    }

    private fun createStatus(): String = with(binding) {
        return "x: ${tvTarget.x}" +
                "\ntransX: ${tvTarget.translationX}"
    }

    private fun onCenterClicked() = with(binding) {
        Orchestra.launch {
            on(tvTarget).translateTo(tvCenterReference.x, tvCenterReference.y) {
                areCoordinatesCenter = true
            }
        }
    }

    private fun onResetClicked() = with(binding) {
        tvTarget.translationX = 0f
        tvTarget.translationY = 0f
    }

    private fun onTranslateByClicked() = with(binding) {
        animateWithXY { x, y ->
            on(tvTarget).translateBy(x, y)
        }
    }

    private fun onTranslateToClicked() = with(binding) {
        animateWithXY { x, y ->
            on(tvTarget).translateTo(x, y)
        }
    }

    private fun onTranslateToReferenceClicked() = with(binding) {
        val referenceView = getReferenceView()

        Orchestra.launch {
            on(tvTarget)
                .translateTo(leftOf(referenceView))
        }
    }

    private fun getReferenceView(): View = with(binding) {
        return when {
            rbParent.isChecked -> tvTarget.rootView
            rbCenterReference.isChecked -> tvCenterReference

            else -> throw IllegalStateException("not implemented")
        }
    }

    private fun animateWithXY(function: OrchestraContext.(Float, Float) -> Unit) = with(binding) {
        val x = etX.text.toString().toIntOrNull()
        val y = etY.text.toString().toIntOrNull()

        if (x != null && y != null) {
            Orchestra.launch { function(this, x.toFloat(), y.toFloat()) }
        }
    }

}
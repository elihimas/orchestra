package com.elihimas.orchestra.examples.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.examples.R
import com.elihimas.orchestra.examples.databinding.ActivityTranslateBinding
import com.elihimas.orchestra.references.TranslationReference
import com.elihimas.orchestra.references.bottomToBottomOf
import com.elihimas.orchestra.references.bottomToTopOf
import com.elihimas.orchestra.references.endToEndOf
import com.elihimas.orchestra.references.endToStartOf
import com.elihimas.orchestra.references.startToEndOf
import com.elihimas.orchestra.references.startToStartOf
import com.elihimas.orchestra.references.topToBottomOf
import com.elihimas.orchestra.references.topToTopOf

enum class TranslateMode {
    TranslateBy, TranslateTo, TranslateByReference
}

class TranslateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTranslateBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initOrchestra()

        binding.root.post {
            updateStatus()
        }

        showControlsFor(TranslateMode.TranslateBy)
    }

    private fun initViews() = with(binding) {
        tvCenterReference.setOnClickListener { onCenterClicked() }

        rbTranslateBy.setOnSelectedListener { showControlsFor(TranslateMode.TranslateBy) }
        rbTranslateTo.setOnSelectedListener { showControlsFor(TranslateMode.TranslateTo) }
        rbTranslateToReference.setOnSelectedListener { showControlsFor(TranslateMode.TranslateByReference) }

        tvStatus.setOnClickListener { updateStatus() }

        btReset.setOnClickListener { onResetClicked() }
        btTranslate.setOnClickListener { onTranslateClicked() }

        spHorizontalReference.adapter = createAdapterWithOptions(R.array.translate_references)
        spVerticalReference.adapter = createAdapterWithOptions(R.array.translate_references)

        spHorizontalTranslateType.adapter =
            createAdapterWithOptions(R.array.horizontal_translate_type)
        spVerticalTranslateType.adapter =
            createAdapterWithOptions(R.array.vertical_translate_type)
    }

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(rightMarker, leftMarker, topMarker, bottomMarker).follows(tvTarget)
        }
    }

    private fun showControlsFor(mode: TranslateMode) = with(binding) {
        positionalControlsContainer.isVisible = mode != TranslateMode.TranslateByReference
        refereceControlsContainer.isVisible = mode == TranslateMode.TranslateByReference
    }

    private fun createAdapterWithOptions(optionsId: Int): SpinnerAdapter {
        return ArrayAdapter.createFromResource(
            this@TranslateActivity,
            optionsId,
            android.R.layout.simple_spinner_item
        )
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
    }

    private fun onTranslateClicked() = with(binding) {
        val translationType = getSelectedTranslationType()

        when (translationType) {
            TranslateMode.TranslateBy -> handleTranslateBy()
            TranslateMode.TranslateTo -> handleTranslateTo()
            TranslateMode.TranslateByReference -> handleTranslateToReference()
        }
    }

    private fun getSelectedTranslationType(): TranslateMode = with(binding) {
        return when {
            rbTranslateBy.isChecked -> TranslateMode.TranslateBy
            rbTranslateTo.isChecked -> TranslateMode.TranslateTo
            else -> TranslateMode.TranslateByReference
        }
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
        val resetableViews = listOf(tvTarget, rightMarker, leftMarker, topMarker, bottomMarker)
        resetableViews.forEach { view ->
            view.translationX = 0f
            view.translationY = 0f
        }
    }

    private fun handleTranslateBy() = with(binding) {
        animateWithXY { x, y ->
            on(tvTarget).translateBy(x, y)
        }
    }

    private fun handleTranslateTo() = with(binding) {
        animateWithXY { x, y ->
            on(tvTarget).translateTo(x, y)
        }
    }

    private fun handleTranslateToReference() = with(binding) {
        val horizontalReference = getHorizontalReference()
        val verticalReference = getVerticalReference()

        val reference = if (horizontalReference != null && verticalReference != null) {
            horizontalReference + verticalReference
        } else {
            horizontalReference ?: verticalReference
        }

        if (reference != null) {
            Orchestra.launch {
                on(tvTarget)
                    .translateTo(reference)
            }
        }
    }

    private fun getHorizontalReference(): TranslationReference? {
        val referenceView = getReferenceFor(binding.spHorizontalReference)

        return if (referenceView != null) {
            when (binding.spHorizontalTranslateType.selectedItemPosition) {
                0 -> null
                1 -> startToStartOf(referenceView)
                2 -> startToEndOf(referenceView)
                3 -> endToStartOf(referenceView)
                4 -> endToEndOf(referenceView)
                else -> TODO()
            }
        } else {
            null
        }
    }

    private fun getVerticalReference(): TranslationReference? {
        val referenceView = getReferenceFor(binding.spVerticalReference)

        return if (referenceView != null) {
            when (binding.spVerticalTranslateType.selectedItemPosition) {
                0 -> null
                1 -> topToTopOf(referenceView)
                2 -> topToBottomOf(referenceView)
                3 -> bottomToTopOf(referenceView)
                4 -> bottomToBottomOf(referenceView)
                else -> TODO()
            }
        } else {
            null
        }
    }

    private fun getReferenceFor(spinner: Spinner): View? = with(binding) {
        return when (spinner.selectedItemPosition) {
            0 -> null
            1 -> tvTarget.rootView
            2 -> tvCenterReference
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

private fun RadioButton.setOnSelectedListener(function: () -> Unit) {
    setOnCheckedChangeListener { _, checked ->
        if (checked) {
            function()
        }
    }
}

package com.elihimas.orchestra.examples.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.examples.databinding.DirectionsSelectorBinding
import com.elihimas.orchestra.examples.databinding.InterpolatorSelectorBinding
import java.lang.IllegalStateException

class InterpolatorSelector(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding by lazy {
        InterpolatorSelectorBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    init {
        binding
    }

    fun getSelectedInterpolator(): Interpolator = when {
        binding.rbLinear.isChecked -> LinearInterpolator()
        binding.rbAccelerate.isChecked -> AccelerateInterpolator()
        binding.rbAccelerateDecelerate.isChecked -> AccelerateDecelerateInterpolator()
        binding.rbAnticipate.isChecked -> AnticipateInterpolator()
        binding.rbAnticipateOvershoot.isChecked -> AnticipateOvershootInterpolator()
        binding.rbBounce.isChecked -> BounceInterpolator()
        binding.rbDecelerate.isChecked -> DecelerateInterpolator()
        binding.rbFastOutLinearIn.isChecked -> FastOutLinearInInterpolator()
        binding.rbFastOutSlowInInterpolator.isChecked -> FastOutSlowInInterpolator()
        binding.rbLinearOutSlowIn.isChecked -> LinearOutSlowInInterpolator()
        binding.rbOvershoot.isChecked -> OvershootInterpolator()

        else -> throw IllegalStateException("Not implemented")
    }


}
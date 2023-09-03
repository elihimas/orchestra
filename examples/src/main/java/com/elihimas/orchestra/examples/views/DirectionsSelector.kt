package com.elihimas.orchestra.examples.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.examples.databinding.DirectionsSelectorBinding

class DirectionsSelector(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding by lazy {
        DirectionsSelectorBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    init {
        binding
    }

    fun getSelectedDirection() = when {
        binding.rbUp.isChecked -> Direction.Up
        binding.rbDown.isChecked -> Direction.Down
        binding.rbLeft.isChecked -> Direction.Left
        else -> Direction.Right
    }


}
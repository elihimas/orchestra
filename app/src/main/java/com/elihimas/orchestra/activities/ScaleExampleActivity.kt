package com.elihimas.orchestra.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.databinding.ActivityScaleExampleBinding

class ScaleExampleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScaleExampleBinding.inflate(layoutInflater) }

    private val directions = mutableListOf("all").apply {
        addAll(Direction.values().map { it.toString() }.toList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() = with(binding) {
        spDirection.adapter =
            ArrayAdapter(this@ScaleExampleActivity, android.R.layout.simple_list_item_1, directions)

        btScale.setOnClickListener {
            scale()
        }
    }

    private fun scale() = with(binding) {
        val directionStr = spDirection.selectedItem.toString()
        val direction = mapDirection(directionStr)
        val duration = etDuration.text.toString().toLongOrNull() ?: 2400
        val scale = etScale.text.toString().toFloatOrNull() ?: 1f

        Orchestra.launch {
            if (direction == null) {
                on(ivExampleImage)
                    .scale(scale) {
                        this.duration = duration
                    }
            } else {
                on(ivExampleImage)
                    .scale(scale, direction) {
                        this.duration = duration
                    }
            }
        }
    }

    private fun mapDirection(directionStr: String) =
        Direction.values().firstOrNull { it.toString() == directionStr }
}
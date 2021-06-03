package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import com.elihimas.orchestra.animations.Direction
import kotlinx.android.synthetic.main.activity_scale_example.*

class ScaleExampleActivity : AppCompatActivity() {

    val directions = mutableListOf("all").apply {
        addAll(Direction.values().map { it.toString() }.toList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_example)

        init()
    }

    private fun init() {
        spDirection.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, directions)

        Orchestra.launch {
            on(ivExampleImage)
                    .scale(4, Direction.Right)
                    .scale(2, Direction.Down)
                    .scale(1)
        }
        btScale.setOnClickListener {
            scale()
        }
    }

    private fun scale() {
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
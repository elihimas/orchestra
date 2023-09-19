package com.elihimas.orchestra.examples.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivityScalingBinding

class ScalingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScalingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener { animate() }
    }

    private fun animate() {
        Orchestra.launch {
            on(binding.targetView)
                .scale(3f) {
                    duration = 1_200
                }
        }
    }
}
package com.elihimas.orchestra.examples.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivityMultipleTargetsBinding

class MultipleTargetsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMultipleTargetsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.btRun.setOnClickListener { runAnimations() }
    }

    private fun runAnimations() = with(binding) {
        Orchestra.launch {
            on(squareRotate1, squareRotate2).rotateBy(90f)
        }
    }
}
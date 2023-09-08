package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivityFadeBinding
import com.elihimas.orchestra.examples.databinding.ExampleTargetBinding

class FadeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFadeBinding.inflate(layoutInflater) }
    private val exampleTargetBinding by lazy { ExampleTargetBinding.bind(binding.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        btFadein.setOnClickListener { onFadeInClicked() }
        btFadeout.setOnClickListener { onFadeOutClicked() }
    }

    private fun onFadeInClicked() = with(exampleTargetBinding) {
        Orchestra.launch {
            on(exampleTarget).fadeIn()
        }
    }

    private fun onFadeOutClicked() = with(exampleTargetBinding) {
        Orchestra.launch {
            on(exampleTarget).fadeOut()
        }
    }
}
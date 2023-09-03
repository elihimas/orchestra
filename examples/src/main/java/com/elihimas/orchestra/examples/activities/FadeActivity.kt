package com.elihimas.orchestra.examples.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.examples.databinding.ActivityFadeBinding

class FadeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFadeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        btFadein.setOnClickListener { onFadeInClicked() }
        btFadeout.setOnClickListener { onFadeOutClicked() }
    }

    private fun onFadeInClicked() = with(binding) {
        Orchestra.launch {
            on(fadeoutTarget).fadeIn()
        }
    }

    private fun onFadeOutClicked() = with(binding) {
        Orchestra.launch {
            on(fadeoutTarget).fadeOut()
        }
    }
}
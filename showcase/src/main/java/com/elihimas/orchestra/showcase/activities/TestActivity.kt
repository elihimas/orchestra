package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.showcase.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupOrchestra()
        setupViews()
    }

    private fun setupOrchestra() {
        with(binding) {
            Orchestra.setup {
                on(tv1).scaleY(0f)
            }
        }
    }

    private fun setupViews() = with(binding) {
        btExpand.setOnClickListener {
            Orchestra.launch {

                on(tv1)
                    .scaleY(1f) {
                        duration = 600
                    }

            }
        }

        btContract.setOnClickListener {
            Orchestra.launch {
                on(tv1)
                    .scaleY(0f) {
                        duration = 600
                    }
            }
        }
    }
}
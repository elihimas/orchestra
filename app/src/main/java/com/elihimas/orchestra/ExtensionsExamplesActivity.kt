package com.elihimas.orchestra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_extensions_example.*

class ExtensionsExamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extensions_example)
        init()
    }

    private fun init() {
        btTextFade1.setOnClickListener {
            tvCrossFade1.setTextFading(R.string.extension_initial_text)
            tvCrossFade2.setTextFading(R.string.extension_initial_text)
        }

        btTextFade2.setOnClickListener {
            tvCrossFade1.setTextFading(R.string.extension_final_text)
            tvCrossFade2.setTextFading(R.string.extension_final_text)
        }

        btTextScale1.setOnClickListener {
            tvScale1.setTextScaling(R.string.extension_initial_text)
            tvScale2.setTextScaling(R.string.extension_initial_text)
        }

        btTextScale2.setOnClickListener {
            tvScale1.setTextScaling(R.string.extension_final_text)
            tvScale2.setTextScaling(R.string.extension_final_text)
        }

        btImageScale1.setOnClickListener {
            ivScale.setImageResourceScaling(R.drawable.butterfly)
        }
        btImageScale2.setOnClickListener {
            ivScale.setImageResourceScaling(R.drawable.two_stick_persons)
        }
    }
}
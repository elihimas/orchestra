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
        btTextCrossFade.setOnClickListener {
            tvCrossFade1.setTextFading("texto modificado")
            tvCrossFade2.setTextFading("texto modificado")
        }

        btTextScale.setOnClickListener {
            tvScale1.setTextScaling("texto modificado")
            tvScale2.setTextScaling("texto modificado")
        }

        btImageScale.setOnClickListener {
            ivScale.setImageResourceScaling(R.drawable.butterfly)
        }
    }
}
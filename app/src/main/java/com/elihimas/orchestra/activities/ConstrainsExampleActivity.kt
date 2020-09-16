package com.elihimas.orchestra.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_constrains_example.*

class ConstrainsExampleActivity : AppCompatActivity() {

    private var isAlt = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constrains_example)

        butterflyImage.setOnClickListener {
            animate()
        }

        Toast.makeText(this, R.string.constrains_layout_hint, Toast.LENGTH_LONG).show()
    }

    private fun animate() {
        val nextLayout = if (isAlt) {
            R.layout.activity_constrains_example
        } else {
            R.layout.activity_constrains_example_alt
        }

        isAlt = !isAlt

        Orchestra.launch {
            changeConstrains(root, nextLayout)
        }
    }
}

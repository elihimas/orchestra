package com.elihimas.orchestra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_form_example.*

class FormExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_example)

        runAnimation()
    }

    private fun runAnimation() {
        val slideViews = arrayOf(titleText, loginText, loginEditText, passwordText, passwordEditText, loginButton)

        Orchestra.setup {
            on(*slideViews).slideHide()

            on(form)
                    .alpha(0)
                    .scale(0.1f)
        }

        Orchestra.launch {
            on(form).parallel {
                fadeIn()
                scale(1f)
            }.duration(200)

            on(*slideViews)
                    .slide()
                    .duration(380)
                    .spacing(120)
        }
    }
}

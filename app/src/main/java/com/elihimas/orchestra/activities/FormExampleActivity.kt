package com.elihimas.orchestra.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import com.elihimas.orchestra.animations.Direction
import kotlinx.android.synthetic.main.activity_form_example.*

class FormExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_example)

        runAnimation()
    }


    private fun runAnimation() {
        val slideViews = arrayOf(titleText, loginText, loginEditText, passwordText, passwordEditText, loginButton)
        form.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


        Orchestra.setup {
            on(form)
                    .invisible()
                    .alpha(0.0f)
            on(*slideViews)
                    .invisible()
        }

        Orchestra.launch {
            delay(500)

            parallel {
                val slideTranslateDuration = 600L
                val halfSlideTranslateDuration = slideTranslateDuration / 2

                on(form).parallel {
                    slide(Direction.Up) {
                        duration = slideTranslateDuration
                    }
                    fadeIn() {
                        duration = halfSlideTranslateDuration
                    }
                }

                on(*slideViews)
                        .delay(halfSlideTranslateDuration)
                        .slide(Direction.Up) {
                            duration = slideTranslateDuration
                        }
            }

        }
    }
}

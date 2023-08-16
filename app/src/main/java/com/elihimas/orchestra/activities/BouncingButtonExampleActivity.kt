package com.elihimas.orchestra.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import com.elihimas.orchestra.actions.WaitAnimateDuration
import com.elihimas.orchestra.databinding.ActivityBoucingButtonExampleBinding

class BouncingButtonExampleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBoucingButtonExampleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        Orchestra.setup {
            on(binding.clickButton1)
                .bounce {
                    duration = WaitAnimateDuration(1000, 1500)
                }
                .onClick {
                    buttonClicked()
                }
        }
    }

    private fun buttonClicked() {
        Toast
            .makeText(
                this@BouncingButtonExampleActivity,
                R.string.at_last_message,
                Toast.LENGTH_LONG
            )
            .show()
    }
}

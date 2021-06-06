package com.elihimas.orchestra.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.R
import com.elihimas.orchestra.actions.WaitAnimateDuration
import kotlinx.android.synthetic.main.activity_boucing_button_example.*

class BouncingButtonExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boucing_button_example)

        init()
    }

    private fun init() {
        Orchestra.setup {
            on(clickButton1)
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
                .makeText(this@BouncingButtonExampleActivity, R.string.at_last_message, Toast.LENGTH_LONG)
                .show()
    }
}

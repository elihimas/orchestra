package com.elihimas.orchestra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.R
import com.elihimas.orchestra.activities.adapers.SplashAdapter
import kotlinx.android.synthetic.main.activity_splash_example.*

class SplashExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_example)

        init()
    }

    private fun init() {
        rvExamples.adapter = SplashAdapter()
    }
}
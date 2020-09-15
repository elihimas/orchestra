package com.elihimas.orchestra.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.R
import com.elihimas.orchestra.model.Examples
import com.elihimas.orchestra.usecases.ButterflyImageConfigurator
import com.elihimas.orchestra.usecases.ConfigViewConfigurator
import kotlinx.android.synthetic.main.activity_centered_butterfly.*

abstract class CenteredImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centered_butterfly)

        init()
    }

    private fun init() {
        val example = getExample()
        ConfigViewConfigurator(configView).execute(example)
        ButterflyImageConfigurator(butterflyImage).execute(example)

        configView.onAnimate {
            runAnimation()
        }
    }

    abstract fun getExample(): Examples

    abstract fun runAnimation()

}

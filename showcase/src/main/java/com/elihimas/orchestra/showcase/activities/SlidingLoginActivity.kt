package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivitySlidingLoginBinding
import com.elihimas.orchestra.showcase.databinding.TvCodeAndRunningControlsBinding

class SlidingLoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySlidingLoginBinding.inflate(layoutInflater) }
    private val runControlsBinding by lazy { TvCodeAndRunningControlsBinding.bind(binding.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        runAnimation()
    }

    private fun initViews() {
        with(runControlsBinding.runAndCodeControls){
            setRunAnimationListener(::runAnimation)
            setCodeText(R.string.code_slide)
        }
    }

    private fun runAnimation() = with(binding) {
        val slidingViews = arrayOf(
            tvTitle,
            loginText,
            etLogin,
            tvPassword,
            etPassword,
            btLogin
        )

        Orchestra.setup {
            on(form, *slidingViews).invisible()
            runControlsBinding.runAndCodeControls.hideWithContext(this)
        }

        Orchestra.launch {
            delay(500)

            parallel {
                on(form).parallel {
                    slideIn(Direction.Up) {
                        duration = 600
                    }
                    fadeIn {
                        duration = 300
                    }
                }

                on(*slidingViews)
                    .delay(300)
                    .slideIn(Direction.Up) {
                        duration = 600
                    }
            }

            runControlsBinding.runAndCodeControls.fadeInWithContext(this)
        }
    }

}
package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivityScalingLoginBinding
import com.elihimas.orchestra.showcase.databinding.TvCodeAndRunningControlsBinding

class ScalingLoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScalingLoginBinding.inflate(layoutInflater) }
    private val runControlsBinding by lazy { TvCodeAndRunningControlsBinding.bind(binding.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initOrchestra()
        initViews()

        runAnimation()
    }

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(tvTitle)
                .scale(2f)
                .bottomToTopOf(form)

            on(container).alpha(0)
            on(form).scale(scaleY = 0f)

            runControlsBinding.runAndCodeControls.hideWithContext(this)
        }
    }

    private fun initViews() {
        with(runControlsBinding.runAndCodeControls){
            setRunAnimationListener(::runAnimation)
            setCodeText(R.string.code_scale)
        }
    }

    private fun runAnimation() = with(binding) {
        initOrchestra()

        Orchestra.launch {
            delay(1_000)
            on(tvTitle).scale(1f) {
                duration = 1_000
                interpolator = AccelerateInterpolator()
            }

            delay(200)

            parallel {
                on(form).scale(1f) {
                    duration = 300
                }

                on(container)
                    .delay(200)
                    .fadeIn {
                        duration = 500
                    }
            }

            runControlsBinding.runAndCodeControls.fadeInWithContext(this)
        }
    }

}
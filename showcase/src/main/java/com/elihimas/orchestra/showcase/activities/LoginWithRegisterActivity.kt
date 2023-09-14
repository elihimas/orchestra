package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.references.startToStartOf
import com.elihimas.orchestra.references.verticalCenterOf
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivityLoginWithRegisterBinding

class LoginWithRegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginWithRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initOrchestra()
    }

    private fun initViews() = with(binding) {
        btRegister.setOnClickListener {
            navigateToRegister()
        }

        btLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun initOrchestra() = with(binding) {
        // TODO: create an orchestra's setup action for translation relative to other view
        binding.root.post {
            registerContainer.translationX = loginContainer.width.toFloat()
        }

        Orchestra.setup {
            on(registerContainer)
                .followsHorizontally(loginContainer)
        }
    }


    private fun navigateToLogin() = with(binding) {
        Orchestra.launch {
            parallel {
                on(loginContainer).slideIn(Direction.Right) {
                    duration = 600
                    startFromCurrentPosition = true
                }

                on(btLogin)
                    .parallel {
                        rotateTo(0f) {
                            duration = 600
                        }
                        resetTranslation {
                            duration = 600
                        }
                    }
            }
        }
    }

    private fun navigateToRegister() = with(binding) {
        Orchestra.launch {
            parallel {
                on(loginContainer).slideOut(Direction.Left) {
                    duration = 600
                    remainingSpace = resources.getDimension(R.dimen.margin50)
                    startFromCurrentPosition = true
                }

                on(btLogin)
                    .parallel {
                        rotateTo(90f) {
                            duration = 600
                        }
                        translateTo(
                            startToStartOf(binding.root) + verticalCenterOf(binding.root)
                        ) {
                            duration = 600
                        }
                    }
            }
        }
    }
}

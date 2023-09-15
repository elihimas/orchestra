package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.references.absoluteCenterOf
import com.elihimas.orchestra.references.horizontalCenterOf
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
        val animationDuration = 400L
        
        Orchestra.launch {
            parallel {
                on(loginContainer).slideIn(Direction.Right) {
                    duration = animationDuration
                    startFromCurrentPosition = true
                }

                on(btLogin)
                    .parallel {
                        rotateTo(0f) {
                            duration = animationDuration
                        }
                        resetTranslation {
                            duration = animationDuration
                        }
                        scale(1f){
                            duration = animationDuration
                        }
                    }

                on(btRegister)
                    .parallel {
                        rotateTo(-90f) {
                            duration = animationDuration
                        }
                        resetTranslation {
                            duration = animationDuration
                        }
                        scale(0.7f){
                            duration = animationDuration
                        }
                    }
            }
        }
    }

    private fun navigateToRegister() = with(binding) {
        val animationDuration = 400L

        Orchestra.launch {
            parallel {
                on(loginContainer).slideOut(Direction.Left) {
                    duration = animationDuration
                    remainingSpace = resources.getDimension(R.dimen.login_margin)
                    startFromCurrentPosition = true
                }

                on(btRegister).parallel {
                    translateTo(absoluteCenterOf(btLogin)) {
                        duration = animationDuration
                    }
                    rotateTo(0f) {
                        duration = animationDuration
                    }
                    scale(1f) {
                        duration = animationDuration
                    }
                }

                on(btLogin)
                    .parallel {
                        rotateTo(90f) {
                            duration = animationDuration
                        }
                        translateTo(
                            horizontalCenterOf(leftGuideline) + verticalCenterOf(root)
                        ) {
                            duration = animationDuration
                        }
                        scale(0.7f) {
                            duration = animationDuration
                        }
                    }
            }
        }
    }
}

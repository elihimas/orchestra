package com.elihimas.orchestra.showcase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
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
            on(loginContainer).slideIn(Direction.Right) {
                duration = 600
            }
        }
    }

    private fun navigateToRegister() = with(binding) {
        Orchestra.launch {
            on(loginContainer).slideOut(Direction.Left) {
                duration = 600
                remainingSpace = resources.getDimension(R.dimen.margin50)
            }
        }
    }
}

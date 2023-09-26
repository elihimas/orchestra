package com.elihimas.orchestra.showcase.activities.mathgame

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.references.bottomToTopOf
import com.elihimas.orchestra.references.verticalCenterOf
import com.elihimas.orchestra.showcase.activities.mathgame.game.GameActivity
import com.elihimas.orchestra.showcase.databinding.ActivityMathGameMainBinding
import com.elihimas.orchestra.showcase.mathgame.Difficulty

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMathGameMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        runAnimation()
    }

    private fun initViews() = with(binding) {
        btDifficultyEasy.setOnClickListener { onEasyClicked() }
        btDifficultyMedium.setOnClickListener { onMediumClicked() }
        btDifficultyHard.setOnClickListener { onHardClicked() }
    }

    private fun onEasyClicked() = with(binding) {
        Orchestra.launch {
            on(btDifficultyMedium, btDifficultyHard).fadeOut()

            on(btDifficultyEasy)
                .repeat(3, createAnimation().apply {
                    fadeIn {
                        interpolator = AccelerateInterpolator()
                        duration = 250
                    }
                })
        }.andThen {
            openMathGame(Difficulty.Easy)
        }
    }

    private fun onMediumClicked() = with(binding) {
        Orchestra.launch {
            on(btDifficultyEasy, btDifficultyHard).fadeOut()

            delay(200)

            on(btDifficultyMedium).translateTo(verticalCenterOf(btDifficultyEasy)) {
                duration = 800
                interpolator = DecelerateInterpolator()
            }
        }.andThen {
            openMathGame(Difficulty.Medium)
        }
    }

    private fun onHardClicked() = with(binding) {
        Orchestra.launch {
            on(btDifficultyEasy, btDifficultyMedium).parallel {
                translateTo(verticalCenterOf(btDifficultyHard)) {
                    duration = 600
                    interpolator = AccelerateDecelerateInterpolator()
                }

                fadeOut {
                    delay = 600
                }
            }

            delay(400)
        }.andThen {
            openMathGame(Difficulty.Hard)
        }
    }

    private fun openMathGame(difficulty: Difficulty) {
        startActivity(Intent(this, GameActivity::class.java))
    }

    private fun runAnimation() = with(binding) {
        val fadeInViews =
            listOf(tvChooseDifficulty, btDifficultyEasy, btDifficultyMedium, btDifficultyHard)

        Orchestra.setup {
            on(fadeInViews).fadeOut()
        }

        Orchestra.launch {
            delay(800)
            parallel {
                on(tvGame).translateTo(bottomToTopOf(tvChooseDifficulty)) {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 800
                }

                on(fadeInViews).fadeIn {
                    delay = 300
                    timeSpacing = 60
                }
            }
        }
    }

}
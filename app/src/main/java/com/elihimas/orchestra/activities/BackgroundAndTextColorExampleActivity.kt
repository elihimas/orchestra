package com.elihimas.orchestra.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.databinding.ActivityBackgroundAndTextColorExampleBinding

data class BackgroundAndTextColors(val backgrounds: List<Int>, val textColors: List<Int>? = null)

class BackgroundAndTextColorExampleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBackgroundAndTextColorExampleBinding.inflate(
            layoutInflater
        )
    }

    private val rootColors = BackgroundAndTextColors(
        listOf(
            android.R.color.holo_red_light,
            android.R.color.holo_red_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_light,
            android.R.color.holo_green_dark,
            android.R.color.holo_purple,
            android.R.color.holo_blue_dark,
            android.R.color.holo_blue_light,
            android.R.color.holo_blue_bright,
        )
    )

    private val colors =
        listOf(
            BackgroundAndTextColors(
                listOf(
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_green_light,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_green_dark
                ),
                listOf(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_purple,
                    android.R.color.holo_blue_dark,
                    android.R.color.white
                )
            ),
            BackgroundAndTextColors(
                listOf(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_red_dark,
                    android.R.color.darker_gray,
                    android.R.color.black,
                    android.R.color.holo_green_light
                ),
                listOf(
                    android.R.color.holo_red_light,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_red_light,
                    android.R.color.white
                )
            ),
            BackgroundAndTextColors(
                listOf(
                    android.R.color.holo_red_dark,
                    android.R.color.holo_green_light,
                    android.R.color.holo_blue_dark
                ),
                listOf(android.R.color.holo_red_light, android.R.color.white)
            ),
            BackgroundAndTextColors(
                listOf(
                    android.R.color.holo_purple,
                    android.R.color.holo_blue_light
                )
            ),
            BackgroundAndTextColors(
                listOf(android.R.color.holo_red_dark, android.R.color.holo_blue_bright),
                listOf(android.R.color.holo_blue_bright, android.R.color.white)
            ),
            BackgroundAndTextColors(
                listOf(
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_purple
                )
            ),
            BackgroundAndTextColors(
                listOf(android.R.color.holo_green_light, android.R.color.holo_red_light),
                listOf(android.R.color.holo_blue_dark, android.R.color.white)
            ),
            BackgroundAndTextColors(
                listOf(android.R.color.holo_green_dark, android.R.color.holo_red_dark),
                listOf(android.R.color.holo_blue_dark, android.R.color.white)
            ),
            BackgroundAndTextColors(
                listOf(android.R.color.holo_orange_light),
                listOf(android.R.color.holo_blue_dark, android.R.color.white)
            ),
            BackgroundAndTextColors(
                listOf(android.R.color.holo_orange_dark),
                listOf(android.R.color.holo_blue_dark, android.R.color.white)
            ),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        Orchestra.launch {
            supportActionBar?.let {
                // on(it).changeBackground(*rootColors.backgrounds.reversed().toIntArray())
            }
        }
        //supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(android.R.color.holo_orange_light)))
    }

    fun onBottomClicked(source: View) {
        animateBackground(source, rootColors)
    }

    fun onViewClicked(source: View) {
        val indexString = source.tag as String
        val index: Int = indexString.toInt()
        val backgroundAndTextColors = colors[index]
        animateBackground(source, backgroundAndTextColors)
    }

    private fun animateBackground(view: View, backgroundAndTextColors: BackgroundAndTextColors) {
        val colorDuration = 2000L
        val foregroundEndAnticipation = 1000L

        val backgrounds = backgroundAndTextColors.backgrounds.toIntArray()
        val textColors = backgroundAndTextColors.textColors?.toIntArray()

        Orchestra.launch {
            on(view).changeBackground(*backgrounds) {
                duration = backgrounds.size * colorDuration
            }

            on(view).parallel {

                textColors?.let {
                    changeTextColor(*textColors) {
                        duration = (textColors.size * colorDuration) - foregroundEndAnticipation
                    }
                }
            }
        }
    }

}

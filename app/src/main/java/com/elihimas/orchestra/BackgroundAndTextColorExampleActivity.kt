package com.elihimas.orchestra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

data class BackgroundAndTextColors(val backgrounds: List<Int>, val textColors: List<Int>? = null)

class BackgroundAndTextColorExampleActivity : AppCompatActivity() {

    private val colors =
            listOf(
                    BackgroundAndTextColors(
                            listOf(android.R.color.holo_red_light, android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_green_light, android.R.color.holo_blue_dark, android.R.color.holo_green_dark),
                            listOf(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_blue_dark, android.R.color.white)),
                    BackgroundAndTextColors(
                            listOf(android.R.color.holo_blue_bright, android.R.color.holo_red_dark, android.R.color.darker_gray, android.R.color.black, android.R.color.holo_green_light),
                            listOf(android.R.color.holo_red_light, android.R.color.holo_red_dark, android.R.color.holo_red_light, android.R.color.white)),
                    BackgroundAndTextColors(
                            listOf(android.R.color.holo_red_dark, android.R.color.holo_green_light, android.R.color.holo_blue_dark),
                            listOf(android.R.color.holo_red_light, android.R.color.white)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_purple, android.R.color.holo_blue_light)),
                    BackgroundAndTextColors(
                            listOf(android.R.color.holo_red_dark, android.R.color.holo_blue_bright),
                            listOf(android.R.color.holo_blue_bright, android.R.color.white)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_orange_light, android.R.color.holo_green_dark, android.R.color.holo_purple)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_green_light, android.R.color.holo_red_light),
                            listOf(android.R.color.holo_blue_dark, android.R.color.white)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_green_dark, android.R.color.holo_red_dark),
                            listOf(android.R.color.holo_blue_dark, android.R.color.white)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_orange_light),
                            listOf(android.R.color.holo_blue_dark, android.R.color.white)),
                    BackgroundAndTextColors(listOf(android.R.color.holo_orange_dark),
                            listOf(android.R.color.holo_blue_dark, android.R.color.white)),
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_and_text_color_example)
    }

    fun onViewClicked(source: View) {
        val indexString = source.tag as String
        val index: Int = indexString.toInt()
        animateBackground(source, index)
    }

    private fun animateBackground(view: View, index: Int) {
        val backgroundAndTextColors = colors[index]
        val colorDuration = 2500L
        val foregroundAnticipation = 1000L

        Orchestra.launch {
            on(view).parallel {
                val backgrounds = backgroundAndTextColors.backgrounds.toIntArray()
                val textColors = backgroundAndTextColors.textColors?.toIntArray()

                changeBackground(*backgrounds) {
                    duration = backgrounds.size * colorDuration
                }

                textColors?.let {
                    changeTextColor(*textColors) {
                        duration = (textColors.size * colorDuration) - foregroundAnticipation
                    }
                }
            }
        }
    }

}

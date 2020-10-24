package com.elihimas.orchestra.activities

import android.R
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra

data class BackgroundAndTextColors(val backgrounds: List<Int>, val textColors: List<Int>? = null)

class BackgroundAndTextColorExampleActivity : AppCompatActivity() {

    private val rootColors = BackgroundAndTextColors(listOf(
            R.color.holo_red_light,
            R.color.holo_red_dark,
            R.color.holo_orange_dark,
            R.color.holo_orange_light,
            R.color.holo_green_light,
            R.color.holo_green_dark,
            R.color.holo_purple,
            R.color.holo_blue_dark,
            R.color.holo_blue_light,
            R.color.holo_blue_bright,
    ))

    private val colors =
            listOf(
                    BackgroundAndTextColors(
                            listOf(R.color.holo_red_light, R.color.holo_orange_dark, R.color.holo_red_dark, R.color.holo_green_light, R.color.holo_blue_dark, R.color.holo_green_dark),
                            listOf(R.color.holo_blue_bright, R.color.holo_purple, R.color.holo_blue_dark, R.color.white)),
                    BackgroundAndTextColors(
                            listOf(R.color.holo_blue_bright, R.color.holo_red_dark, R.color.darker_gray, R.color.black, R.color.holo_green_light),
                            listOf(R.color.holo_red_light, R.color.holo_red_dark, R.color.holo_red_light, R.color.white)),
                    BackgroundAndTextColors(
                            listOf(R.color.holo_red_dark, R.color.holo_green_light, R.color.holo_blue_dark),
                            listOf(R.color.holo_red_light, R.color.white)),
                    BackgroundAndTextColors(listOf(R.color.holo_purple, R.color.holo_blue_light)),
                    BackgroundAndTextColors(
                            listOf(R.color.holo_red_dark, R.color.holo_blue_bright),
                            listOf(R.color.holo_blue_bright, R.color.white)),
                    BackgroundAndTextColors(listOf(R.color.holo_orange_light, R.color.holo_green_dark, R.color.holo_purple)),
                    BackgroundAndTextColors(listOf(R.color.holo_green_light, R.color.holo_red_light),
                            listOf(R.color.holo_blue_dark, R.color.white)),
                    BackgroundAndTextColors(listOf(R.color.holo_green_dark, R.color.holo_red_dark),
                            listOf(R.color.holo_blue_dark, R.color.white)),
                    BackgroundAndTextColors(listOf(R.color.holo_orange_light),
                            listOf(R.color.holo_blue_dark, R.color.white)),
                    BackgroundAndTextColors(listOf(R.color.holo_orange_dark),
                            listOf(R.color.holo_blue_dark, R.color.white)),
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.elihimas.orchestra.R.layout.activity_background_and_text_color_example)

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

//            on(view).parallel {
//
//                textColors?.let {
//                    changeTextColor(*textColors) {
//                        duration = (textColors.size * colorDuration) - foregroundEndAnticipation
//                    }
//                }
//            }
        }
    }

}

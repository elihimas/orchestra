package com.elihimas.orchestra.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.R
import com.elihimas.orchestra.model.Examples
import com.elihimas.orchestra.usecases.AnimatorRunner
import com.elihimas.orchestra.usecases.ButterflyImageConfigurator
import com.elihimas.orchestra.usecases.ConfigViewConfigurator
import kotlinx.android.synthetic.main.activity_centered_butterfly.*
import java.lang.IllegalArgumentException
import kotlin.math.E

class CenteredImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centered_butterfly)

        init()
    }

    private fun init() {
        val example = getExample()
        val titleResId = exampleToTitleId(example)
        supportActionBar?.title = getString(titleResId)

        ConfigViewConfigurator(configView).execute(example)
        ButterflyImageConfigurator(butterflyImage).execute(example)

        configView.onAnimate {
            val animationEnded = {
                configView.enableAnimateButton()
            }
            val runner = AnimatorRunner(configView, butterflyImage, animationEnded)
            runner.execute(example)
        }
    }

    private fun exampleToTitleId(example: Examples) = when (example) {
        Examples.FadeIn -> R.string.fade_in
        Examples.FadeOut -> R.string.fade_out
        Examples.Translate -> R.string.translate
        Examples.Scale -> R.string.scale
        Examples.Slide -> R.string.slide
        Examples.SlideOut -> R.string.slide_out
        Examples.CircularReveal -> R.string.circular_reveal
        else -> throw IllegalArgumentException("title not supported for: $example")
    }

    private fun getExample(): Examples =
            intent.getSerializableExtra(EXAMPLE_EXTRA) as Examples

    companion object {
        const val EXAMPLE_EXTRA = "EXAMPLE_EXTRA"

        private val supportedExamples =
                listOf(Examples.FadeIn, Examples.FadeOut, Examples.Scale,
                        Examples.Slide, Examples.SlideOut, Examples.Translate,
                        Examples.CircularReveal)

        fun startActivity(context: Context, example: Examples) {
            if (!supportedExamples.contains(example)) {
                throw IllegalArgumentException("example with centered image not supported for: $example")
            }

            context.startActivity(Intent(context, CenteredImageActivity::class.java).apply {
                putExtra(EXAMPLE_EXTRA, example)
            })
        }

    }
}

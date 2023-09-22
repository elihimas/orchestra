package com.elihimas.orchestra.examples.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.examples.R
import com.elihimas.orchestra.examples.databinding.ActivityMultipleTargetsBinding
import com.elihimas.orchestra.references.horizontalCenterOf

class MultipleTargetsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMultipleTargetsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initOrchestra()
    }

    private fun initViews() {
        binding.btRun.setOnClickListener { runAnimations() }
    }

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(squareSlideInUp1).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._30dp),
                direction = Direction.Up
            )
            on(squareSlideInUp2).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._20dp),
                direction = Direction.Up
            )

            on(squareSlideInDown1).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._20dp),
                direction = Direction.Down
            )
            on(squareSlideInDown2).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._30dp),
                direction = Direction.Down
            )

            on(squareSlideInRight1).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._30dp),
                direction = Direction.Right
            )
            on(squareSlideInRight2).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._20dp),
                direction = Direction.Right
            )

            on(squareSlideInLeft1).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._30dp),
                direction = Direction.Left
            )
            on(squareSlideInLeft2).setSlideInTo(
                visibleSize = resources.getDimension(R.dimen._20dp),
                direction = Direction.Left
            )
        }
    }

    private fun runAnimations() = with(binding) {
        Orchestra.launch {
            parallel {
                on(squareRotateBy1, squareRotateBy2).rotateBy(90f)

                on(squareRotateTo1, squareRotateTo2).rotateTo(90f)

                on(squareScale1, squareScale2).scale(1f)

                on(squareTranslateTo1, squareTranslateTo2).translateTo(horizontalCenterOf(root))

                on(squareTranslateBy1, squareTranslateBy2).translateBy(80f, -60f)

                on(squareSlideInUp1, squareSlideInUp2).slideIn(Direction.Up) {
                    startFromCurrentPosition = true
                }

                on(squareSlideInDown1, squareSlideInDown2).slideIn(Direction.Down) {
                    startFromCurrentPosition = true
                }


                on(squareSlideInRight1, squareSlideInRight2).slideIn(Direction.Right) {
                    startFromCurrentPosition = true
                }

                on(squareSlideInLeft1, squareSlideInLeft2).slideIn(Direction.Left) {
                    startFromCurrentPosition = true
                }
            }
        }
    }
}
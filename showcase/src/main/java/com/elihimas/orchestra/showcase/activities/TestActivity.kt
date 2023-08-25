package com.elihimas.orchestra.showcase.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.databinding.ActivityTestBinding

// TODO: remove this. This activity is intended to help developing setup operations like topToBottomOf
class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    private val collapsedScale = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupOrchestra()
        setupViews()
    }

    private fun setupOrchestra() {
        with(binding) {
            Orchestra.setup {
                on(scalingView1).scale(collapsedScale, Direction.Down)
                on(scalingView2).scale(collapsedScale, Direction.Up)
                on(horizontalScalingView1).scale(collapsedScale, Direction.Right)
                on(horizontalScalingView2).scale(collapsedScale, Direction.Left)

                on(affectedView1).topToBottomOf(scalingView1)
                on(affectedView2).bottomToTopOf(scalingView2)

                on(horizontalAffectedView1).rightToLeftOf(horizontalScalingView1)
                on(horizontalAffectedView2).leftToRightOf(horizontalScalingView2)
            }
        }
    }

    private fun setupViews() = with(binding) {
        btExpand.setOnClickListener {
            Orchestra.launch {
                parallel {
                    on(scalingView1)
                        .scale(1f, direction = Direction.Down)
                    on(scalingView2)
                        .scale(1f, direction = Direction.Up)

                    on(horizontalScalingView1)
                        .scale(1f, direction = Direction.Right)
                    on(horizontalScalingView2)
                        .scale(1f, direction = Direction.Left)
                }
            }
        }

        btContract.setOnClickListener {
            Orchestra.launch {
                parallel {
                    on(scalingView1)
                        .scale(collapsedScale, direction = Direction.Down)
                    on(scalingView2)
                        .scale(collapsedScale, direction = Direction.Up)

                    on(horizontalScalingView1)
                        .scale(collapsedScale, direction = Direction.Right)
                    on(horizontalScalingView2)
                        .scale(collapsedScale, direction = Direction.Left)
                }
            }
        }

        btX2.setOnClickListener {
            Orchestra.launch {
                parallel {
                    on(scalingView1, scalingView2)
                        .scale(2f)
                }
            }
        }

        btX1.setOnClickListener {
            Orchestra.launch {
                parallel {
                    on(scalingView1, scalingView2)
                        .scale(1f)
                }
            }
        }
    }
}
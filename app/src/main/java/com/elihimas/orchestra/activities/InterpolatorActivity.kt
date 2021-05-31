package com.elihimas.orchestra.activities

import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_interpolator.*

class InterpolatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interpolator)

        init()
    }

    private fun init() {
        Orchestra.launch {
            parallel {
                handleSquare(this, square1, AccelerateDecelerateInterpolator())
                handleSquare(this, square3, AccelerateInterpolator())
                handleSquare(this, square2, AnticipateInterpolator())
                handleSquare(this, square4, BounceInterpolator())
                handleSquare(this, square5, FastOutLinearInInterpolator())
                handleSquare(this, square6, OvershootInterpolator())
            }
        }
    }

    private fun handleSquare(orchestraContext: OrchestraContext, view: View, interpolator: Interpolator) {
        rotate(orchestraContext, view, interpolator)

        view.setOnClickListener {
            Orchestra.launch {
                rotate(this, view, interpolator)
            }
        }
    }

    private fun rotate(orchestraContext: OrchestraContext, view: View, interpolator: Interpolator) {
        orchestraContext
                .on(view)
                .rotate(180.0f) {
                    this.interpolator = interpolator
                    duration = 2400
                }
                .scale(2){
                    this.interpolator = interpolator
                    duration = 2000
                }
                .scale(1){
                    this.interpolator = interpolator
                    duration = 2000
                }
    }
}
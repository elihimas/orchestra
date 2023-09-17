package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

class TranslateToPositionAnimation(private val destinationX: Float, private val destinationY: Float) : Animation() {

    private var initialX = 0f
    private var initialY = 0f
    private var deltaX = 0f
    private var deltaY = 0f

    var areCoordinatesCenter = false

    override fun beforeAnimation(views: List<View>) {
        initialX = views[0].x
        initialY = views[0].y

        if(areCoordinatesCenter) {
            val xIncrement = views[0].width/2
            val yIncrement = views[0].height/2

            deltaX = destinationX - initialX - xIncrement
            deltaY = destinationY - initialY - yIncrement
        }else{
            deltaX = destinationX - initialX
            deltaY = destinationY - initialY
        }
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val newX = initialX + proportion * deltaX
        val newY = initialY + proportion * deltaY

        view.x = newX
        view.y = newY
    }

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return TranslateToPositionAnimation(destinationX, destinationY).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}
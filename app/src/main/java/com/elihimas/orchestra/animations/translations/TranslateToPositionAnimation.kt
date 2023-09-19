package com.elihimas.orchestra.animations.translations

import android.view.View
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

class TranslateToPositionAnimation(
    private val destinationX: Float,
    private val destinationY: Float
) : StatefulAnimation<TranslateToPositionData>() {

    var areCoordinatesCenter = false

    override fun beforeAnimation(views: List<View>) {
        animationDataList = buildList {
            views.forEach { view ->

                val initialX = view.x
                val initialY = view.y
                val deltaX: Float
                val deltaY: Float


                if (areCoordinatesCenter) {
                    val xIncrement = view.width / 2
                    val yIncrement = view.height / 2

                    deltaX = destinationX - initialX - xIncrement
                    deltaY = destinationY - initialY - yIncrement
                } else {
                    deltaX = destinationX - initialX
                    deltaY = destinationY - initialY
                }

                add(TranslateToPositionData(initialX, initialY, deltaX, deltaY))
            }
        }
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: TranslateToPositionData
    ) {
        val newX = animationData.initialX + proportion * animationData.deltaX
        val newY = animationData.initialY + proportion * animationData.deltaY

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
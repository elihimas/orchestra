package com.elihimas.orchestra.animations

import android.view.View

//TODO: handle all directions
class DirectionalScaleAnimation(scaleX: Float, scaleY: Float, var direction: Direction) : ScaleAnimation(scaleX, scaleY) {

    constructor(scale: Float, direction: Direction) : this(scale, scale, direction)

    private var initialHeight = 0
    private var deltaY: Float = 0.0f

    override fun init(vararg views: View) {
        super.init(*views)
        initialHeight = views[0].height
        //TODO: review this. this delta is necessary to handle previous scale operations
        deltaY = ((views[0].height * views[0].scaleY) - views[0].height) / 2
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) {
        val scaleX = initialScaleX + proportion * valueDeltaX
        val yIncrement = proportion * valueDeltaY
        val scaleY = initialScaleY + yIncrement
        view.scaleX = scaleX
        view.scaleY = scaleY

        if (direction == Direction.Up) {
            val addedHeight = initialHeight * yIncrement
            val translation = addedHeight / 2 + deltaY
            view.translationY = -translation
        }

        if (direction == Direction.Down) {
            val addedHeight = initialHeight * yIncrement
            val translation = addedHeight / 2 + deltaY
            view.translationY = translation
        }

        //TODO: handle left and right directions
    }
}
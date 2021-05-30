package com.elihimas.orchestra.animations

import android.view.View

abstract class VerticalStrategy(var scaleY: Float) : AnimationStrategy {
    private var initialScaleY = 1f
    private var initialHeight = 0
    private var deltaY: Float = 0.0f
    private var valueDeltaY = 0f

    override fun init(vararg views: View) {
        initialHeight = views[0].height
        initialScaleY = views[0].scaleY
        valueDeltaY = scaleY - initialScaleY

        //TODO: review this. this delta is necessary to handle previous scale operations
        deltaY = ((views[0].height * views[0].scaleY) - views[0].height) / 2
    }

    override fun update(view: View, proportion: Float) {
        val yIncrement = proportion * valueDeltaY
        val scaleY = initialScaleY + yIncrement
        view.scaleY = scaleY

        val addedHeight = initialHeight * yIncrement
        val translation = addedHeight / 2 + deltaY

        translate(view, translation)
    }

    abstract fun translate(view: View, translation: Float)
}

abstract class HorizontalStrategy(var scaleX: Float) : AnimationStrategy {
    private var initialScaleX = 1f
    private var initialWidth = 0
    private var deltaX: Float = 0.0f
    private var valueDeltaX = 0f

    override fun init(vararg views: View) {
        initialWidth = views[0].width
        initialScaleX = views[0].scaleX
        valueDeltaX = scaleX - initialScaleX

        //TODO: review this. this delta is necessary to handle previous scale operations
        deltaX = ((views[0].width * views[0].scaleX) - views[0].width) / 2
    }

    override fun update(view: View, proportion: Float) {
        val xIncrement = proportion * valueDeltaX
        val scaleX = initialScaleX + xIncrement
        view.scaleX = scaleX

        val addedHeight = initialWidth * xIncrement
        val translation = addedHeight / 2 + deltaX

        translate(view, translation)
    }

    abstract fun translate(view: View, translation: Float)
}

class UpStrategy(scaleY: Float) : VerticalStrategy(scaleY) {

    override fun translate(view: View, translation: Float) {

        view.translationY = -translation
    }
}

class DownStrategy(scaleY: Float) : VerticalStrategy(scaleY) {

    override fun translate(view: View, translation: Float) {
        view.translationY = translation
    }
}

class LeftStrategy(scaleY: Float) : HorizontalStrategy(scaleY) {

    override fun translate(view: View, translation: Float) {
        view.translationX = -translation
    }
}

class RightStrategy(scaleY: Float) : HorizontalStrategy(scaleY) {

    override fun translate(view: View, translation: Float) {
        view.translationX = translation
    }
}

//TODO: handle all directions
class DirectionalScaleAnimation(val scale: Float, var direction: Direction) : Animation() {

    private val animationStrategy: AnimationStrategy = when (direction) {
        Direction.Up -> UpStrategy(scale)
        Direction.Down -> DownStrategy(scale)
        Direction.Left -> LeftStrategy(scale)
        Direction.Right -> RightStrategy(scale)
    }

    override fun init(vararg views: View) {
        animationStrategy.init(*views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) =
            animationStrategy.update(view, proportion)


    override fun clone() = DirectionalScaleAnimation(scale, direction)
}
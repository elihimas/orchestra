package com.elihimas.orchestra.animations.scale

import android.view.View
import com.elihimas.orchestra.animations.AnimationStrategy
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.animations.StatefulAnimation
import com.elihimas.orchestra.constrains.deeffectors.DirectionalScaleAnimationDeEffector

//TODO: fix successive translations translation problem

abstract class VerticalStrategy(private var scaleY: Float) :
    AnimationStrategy<DirectionalScaleData> {

    // TODO: move this to DirectionalScaleData
    private var initialScaleY = 1f
    private var initialHeight = 0
    private var deltaY: Float = 0f
    private var valueDeltaY = 0f

    override fun createAnimationDataFor(views: List<View>): List<DirectionalScaleData> = buildList {
        views.forEach { view ->
            initialHeight = view.height
            initialScaleY = view.scaleY
            valueDeltaY = scaleY - initialScaleY

            //TODO: review this. this delta is necessary to handle previous scale operations
            deltaY = ((view.height * view.scaleY) - view.height) / 2

            add(DirectionalScaleData())
        }
    }

    override fun update(view: View, proportion: Float, animationData: DirectionalScaleData) {
        val yIncrement = proportion * valueDeltaY
        val scaleY = initialScaleY + yIncrement
        view.scaleY = scaleY

        val addedHeight = initialHeight * yIncrement
        val translation = addedHeight / 2 + deltaY

        translate(view, translation)
    }

    abstract fun translate(view: View, translation: Float)
}

abstract class HorizontalStrategy(var scaleX: Float) : AnimationStrategy<DirectionalScaleData> {

    // TODO: move this to DirectionalScaleData
    private var initialScaleX = 1f
    private var initialWidth = 0
    private var deltaX: Float = 0f
    private var valueDeltaX = 0f

    override fun createAnimationDataFor(views: List<View>): List<DirectionalScaleData> = buildList {
        views.forEach { view ->
            initialWidth = view.width
            initialScaleX = view.scaleX
            valueDeltaX = scaleX - initialScaleX

            //TODO: review this. this delta is necessary to handle previous scale operations
            deltaX = ((view.width * view.scaleX) - view.width) / 2

            add(DirectionalScaleData())
        }
    }

    override fun update(view: View, proportion: Float, animationData: DirectionalScaleData) {
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
class DirectionalScaleAnimation(val scale: Float, var direction: Direction) :
    StatefulAnimation<DirectionalScaleData>() {

    private val animationStrategy: AnimationStrategy<DirectionalScaleData> = when (direction) {
        Direction.Up -> UpStrategy(scale)
        Direction.Down -> DownStrategy(scale)
        Direction.Left -> LeftStrategy(scale)
        Direction.Right -> RightStrategy(scale)
    }

    override fun beforeAnimation(views: List<View>) {
        animationDataList = animationStrategy.createAnimationDataFor(views)
    }

    override fun updateAnimationByProportion(
        view: View,
        proportion: Float,
        animationData: DirectionalScaleData
    ) =
        animationStrategy.update(view, proportion, animationData)

    override fun getDeEffector() = DirectionalScaleAnimationDeEffector

    override fun clone() = DirectionalScaleAnimation(scale, direction).also { clone ->
        cloneFromTo(this, clone)
    }
}
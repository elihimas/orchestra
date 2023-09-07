package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

abstract class HorizontalSlideStrategy : AnimationStrategy {

    var initialTranslationX = 0f

    override fun init(vararg views: View) {
        // TODO: verify if this makes sense in some scenario
         initialTranslationX = views[0].translationX
    }
}

abstract class VerticalSlideStrategy : AnimationStrategy {

    var initialTranslationY = 0f

    override fun init(vararg views: View) {
        // TODO: verify if this makes sense in some scenario
        // initialTranslationY = views[0].translationY
    }
}

class SlideInUpStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, down.toInt())
        view.translationY = view.height - down + initialTranslationY
    }
}

class SlideInDownStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, view.height - top.toInt(), view.width, view.height)
        view.translationY = -view.height + top + initialTranslationY
    }

}

class SlideInLeftStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val right = view.width * proportion

        view.clipBounds = Rect(0, 0, right.toInt(), view.height)
        view.translationX = view.width - right + initialTranslationX
    }
}

class SlideInRightStrategy : HorizontalSlideStrategy() {

    override fun init(vararg views: View) {
        super.init(*views)

        // TODO: fix this:
        // this line is necessary for the login with register example but breaks slide in
        val isViewOnLeft = views[0].translationX < 0f

        initialTranslationX = if (isViewOnLeft) {
            views[0].width + views[0].translationX
        } else {
            0f
        }
    }

    override fun update(view: View, proportion: Float) {
        val left = (view.width - initialTranslationX) * proportion + initialTranslationX

        view.clipBounds =
            Rect(view.width - (left.toInt()), 0, view.width, view.height)
        view.translationX = -view.width + left
    }
}

/**
 * Slide in and out animation implementation
 *
 * @param direction the direction towards to the view should be animated
 */
open class SlideAnimation(
    protected var direction: Direction, var remainingWidth: Float = 0f
) : Animation() {

    private lateinit var slideStrategy: AnimationStrategy

    open fun createSlideStrategy(): AnimationStrategy {
        return when (direction) {
            Direction.Up -> SlideInUpStrategy()
            Direction.Down -> SlideInDownStrategy()
            Direction.Left -> SlideInLeftStrategy()
            Direction.Right -> SlideInRightStrategy()
        }
    }

    override fun beforeAnimation(vararg views: View) {
        slideStrategy = createSlideStrategy()

        views.forEach { view -> view.visibility = View.VISIBLE }

        slideStrategy.init(*views)
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) =
        slideStrategy.update(view, proportion)

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return SlideAnimation(direction, remainingWidth).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}
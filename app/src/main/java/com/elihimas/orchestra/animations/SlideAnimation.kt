package com.elihimas.orchestra.animations

import android.graphics.Rect
import android.view.View
import com.elihimas.orchestra.constrains.deeffectors.TranslationDeEffector

abstract class HorizontalSlideStrategy : AnimationStrategy {

    var initialTranslationX = 0f

    override fun init(vararg views: View) {
        // TODO: verify if this makes sense in some scenario
        // initialTranslationX = views[0].translationX
    }
}

abstract class VerticalSlideStrategy : AnimationStrategy {

    var initialTranslationY = 0f

    override fun init(vararg views: View) {
        // TODO: verify if this makes sense in some scenario
        // initialTranslationY = views[0].translationY
    }
}

class SlideOutUpStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val down = view.height * proportion

        view.clipBounds = Rect(0, 0, view.width, (view.height - down).toInt())
        view.translationY = down + initialTranslationY
    }
}

class SlideOutDownStrategy : VerticalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val top = view.height * proportion

        view.clipBounds = Rect(0, top.toInt(), view.width, view.height)
        view.translationY = -top + initialTranslationY
    }
}

class SlideOutLeftStrategy : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val right = view.width * proportion

        view.clipBounds = Rect(0, 0, (view.width - right).toInt(), view.height)
        view.translationX = right + initialTranslationX
    }
}

class SlideOutRightStrategy(private val remainingWidth: Float) : HorizontalSlideStrategy() {

    override fun update(view: View, proportion: Float) {
        val left = view.width * proportion
        val remainingWidthProportion = remainingWidth * proportion

        view.clipBounds =
            Rect((left - remainingWidthProportion).toInt(), 0, view.width, view.height)
        view.translationX = -left + initialTranslationX + remainingWidthProportion
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

    private var initialLeft = 0f
    override fun init(vararg views: View) {
        super.init(*views)

        initialLeft = views[0].width + views[0].translationX
    }

    override fun update(view: View, proportion: Float) {
        val left = (view.width - initialLeft) * proportion + initialLeft

        view.clipBounds =
            Rect(view.width - (left.toInt()), 0, view.width, view.height)
        view.translationX = -view.width + left + initialTranslationX
    }
}

/**
 * Slide in and out animation implementation
 *
 * @param direction the direction towards to the view should be animated
 * @param reverseAnimation if the animation is a slide out animation
 */
open class SlideAnimation(
    internal var direction: Direction,
    private val reverseAnimation: Boolean = false
) : Animation() {

    private lateinit var slideStrategy: AnimationStrategy
    var remainingWidth: Float = 0f

    //TODO create variables for each view
    override fun beforeAnimation(vararg views: View) {
        val actualDirection = if (reverseAnimation) {
            direction.reverse()
        } else {
            direction
        }

        slideStrategy = if (reverseAnimation) {
            when (actualDirection) {
                Direction.Up -> SlideOutUpStrategy()
                Direction.Down -> SlideOutDownStrategy()
                Direction.Left -> SlideOutLeftStrategy()
                Direction.Right -> SlideOutRightStrategy(remainingWidth)
            }
        } else {
            when (actualDirection) {
                Direction.Up -> SlideInUpStrategy()
                Direction.Down -> SlideInDownStrategy()
                Direction.Left -> SlideInLeftStrategy()
                Direction.Right -> SlideInRightStrategy()
            }
        }

        if (!reverseAnimation) {
            views.forEach { view -> view.visibility = View.VISIBLE }
        }

        slideStrategy.init(*views)
    }

    override fun afterAnimation(vararg views: View) {
        // TODO review. maybe there is a better way to verify if there is something visible or not
        if (reverseAnimation && remainingWidth == 0f) {
            views.forEach { view -> view.visibility = View.INVISIBLE }
        }
    }

    override fun updateAnimationByProportion(view: View, proportion: Float) =
        slideStrategy.update(view, proportion)

    override fun getDeEffector() = TranslationDeEffector

    override fun clone(): Any {
        return SlideAnimation(direction, reverseAnimation).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}
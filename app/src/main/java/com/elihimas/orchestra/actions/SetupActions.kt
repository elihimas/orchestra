package com.elihimas.orchestra.actions

import android.view.View
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.animations.slide.SlideData
import com.elihimas.orchestra.animations.slide.SlideInDownStrategy
import com.elihimas.orchestra.animations.slide.SlideInLeftStrategy
import com.elihimas.orchestra.animations.slide.SlideInRightStrategy
import com.elihimas.orchestra.animations.slide.SlideInUpStrategy
import com.elihimas.orchestra.constrains.DeEffector
import com.elihimas.orchestra.constrains.References

abstract class SetupAction {
    fun runSetup(views: Array<out View>) {
        views.forEach { view ->
            view.post {
                runSetup(view)

                val affectedViews = References.map[view]
                affectedViews?.let { affectedViews ->
                    val deEffector = getDeEffector()
                    deEffector?.applyEffect(view, affectedViews)
                }
            }
        }
    }

    abstract fun runSetup(view: View)
    open fun getDeEffector(): DeEffector? = null
}

class AlphaAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.alpha = value
    }
}

class InvisibleAction : SetupAction() {
    override fun runSetup(view: View) {
        view.visibility = View.INVISIBLE
    }

}

class OnClick(private val onClickListener: View.OnClickListener) : SetupAction() {
    override fun runSetup(view: View) {
        view.setOnClickListener(onClickListener)
    }

}

class SlideAction(private val visibleSize: Float, private val direction: Direction) :
    SetupAction() {
    override fun runSetup(view: View) {

        when (direction) {
            Direction.Up -> {
                val strategy = SlideInUpStrategy(0f, true)
                strategy.updateVisibleHeight(view, visibleSize)
            }

            Direction.Down -> {
                val strategy = SlideInDownStrategy(0f, true)
                strategy.updateVisibleHeight(view, visibleSize)
            }

            Direction.Left -> {
                val visibleLeft = view.width - visibleSize
                val strategy = SlideInLeftStrategy(0f, true)
                strategy.update(view, 0f, SlideData(visibleLeft))
            }
            Direction.Right -> {
                val visibleRight = view.width - visibleSize
                val strategy = SlideInRightStrategy(0f, true)
                strategy.update(view, 0f, SlideData(visibleRight))
            }
        }
    }

}
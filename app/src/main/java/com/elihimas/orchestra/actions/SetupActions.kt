package com.elihimas.orchestra.actions

import android.view.View
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
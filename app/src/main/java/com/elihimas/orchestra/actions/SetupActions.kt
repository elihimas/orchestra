package com.elihimas.orchestra.actions

import android.view.View
import com.elihimas.orchestra.OrchestraConfiguration

abstract class SetupAction {
    fun runSetup(views: Array<out View>) {
        views.forEach {
            runSetup(it)
        }
    }

    abstract fun runSetup(view: View)
}

class AlphaAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.alpha = value
    }
}

class SetupScaleAction(private val value: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.apply {
            scaleX = value
            scaleY = value
        }
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
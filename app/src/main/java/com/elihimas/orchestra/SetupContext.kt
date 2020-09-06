package com.elihimas.orchestra

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginTop

class SetupContext {

    private val references = mutableListOf<SetupReference>()

    fun on(vararg views: View) = SetupReference(*views).apply {
        references.add(this)
    }

    fun runSetup() {
        references.forEach {
            it.runSetup()
        }
    }

}

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

class SlideHideAction : SetupAction() {
    override fun runSetup(view: View) {
        view.apply {

            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val height = measuredHeight.toFloat()

            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).also {
                it.topMargin += height.toInt()
                it.height = 0
            }
        }
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

class SetupReference(vararg val views: View) {

    private val actions = mutableListOf<SetupAction>()

    open fun add(action: SetupAction): SetupReference {
        actions.add(action)

        return this
    }

    fun alpha(value: Float) = add(AlphaAction(value))
    fun alpha(value: Int) = alpha(value.toFloat())

    fun slideHide() = add(SlideHideAction())

    fun scale(value: Float) = add(SetupScaleAction(value))

    fun runSetup() {
        actions.forEach {
            it.runSetup(views)
        }
    }

}

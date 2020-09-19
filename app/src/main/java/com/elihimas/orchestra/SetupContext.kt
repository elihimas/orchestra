package com.elihimas.orchestra

import android.view.View

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

class SetupReference(vararg val views: View) {

    private val actions = mutableListOf<SetupAction>()

    open fun add(action: SetupAction): SetupReference {
        actions.add(action)

        return this
    }

    fun alpha(value: Float) = add(AlphaAction(value))
    fun alpha(value: Int) = alpha(value.toFloat())

    fun scale(value: Float) = add(SetupScaleAction(value))

    fun circularRevealHide() = add(CircularRevealHideAction())

    fun runSetup() {
        actions.forEach {
            it.runSetup(views)
        }
    }

}

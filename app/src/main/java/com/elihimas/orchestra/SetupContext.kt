package com.elihimas.orchestra

import android.view.View
import com.elihimas.orchestra.actions.AlphaAction
import com.elihimas.orchestra.actions.BounceAction
import com.elihimas.orchestra.actions.DirectionalScaleSetupAction
import com.elihimas.orchestra.actions.InvisibleAction
import com.elihimas.orchestra.actions.OnClick
import com.elihimas.orchestra.actions.ScaleSetupAction
import com.elihimas.orchestra.actions.SetupAction
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.constrains.AffectedViews
import com.elihimas.orchestra.constrains.Constrain
import com.elihimas.orchestra.constrains.References
import java.lang.ref.WeakReference

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

class SetupReference(private vararg val views: View) {

    private val actions = mutableListOf<SetupAction>()

    private fun add(action: SetupAction): SetupReference {
        actions.add(action)

        return this
    }

    fun alpha(value: Float) = add(AlphaAction(value))
    fun alpha(value: Int) = alpha(value.toFloat())

    fun scale(value: Float) = scale(scaleX = value, scaleY = value)
    fun scale(scaleX: Float = 1f, scaleY: Float = 1f) = add(ScaleSetupAction(scaleX, scaleY))
    fun scale(scale: Float, direction: Direction) =
        add(DirectionalScaleSetupAction(scale, direction))

    fun invisible() = add(InvisibleAction())
    fun bounce() = add(BounceAction())

    //TODO move this to extensions
    fun bounce(config: BounceAction.() -> Unit): SetupReference {
        with(BounceAction()) {
            add(this)
            config.invoke(this)
        }

        return this
    }

    fun onClick(onClickListener: View.OnClickListener) = add(OnClick(onClickListener))

    fun runSetup() {
        actions.forEach { setupAction ->
            setupAction.runSetup(views)
        }
    }

    fun topToBottomOf(targetView: View): SetupReference {
        References.map[targetView] =
            AffectedViews(Constrain.TopToBottomOf, views.map { WeakReference(it) })

        return this
    }

    fun bottomToTopOf(targetView: View): SetupReference {
        References.map[targetView] =
            AffectedViews(Constrain.BottomToTopOf, views.map { WeakReference(it) })

        return this
    }

    fun leftToRightOf(targetView: View): SetupReference {
        References.map[targetView] =
            AffectedViews(Constrain.LeftToRightOf, views.map { WeakReference(it) })

        return this
    }

    fun rightToLeftOf(targetView: View): SetupReference {
        References.map[targetView] =
            AffectedViews(Constrain.RightToLeftOf, views.map { WeakReference(it) })

        return this
    }

}

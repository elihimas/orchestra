package com.elihimas.orchestra.actions

import android.view.View
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.constrains.deeffectors.DirectionalScaleSetupDeEffector
import com.elihimas.orchestra.constrains.deeffectors.ScaleDeSetupEffector

class ScaleSetupAction(private val scaleX: Float, private val scaleY: Float) : SetupAction() {
    override fun runSetup(view: View) {
        view.scaleX = scaleX
        view.scaleY = scaleY
    }

    override fun getDeEffector() = ScaleDeSetupEffector(scaleX, scaleY)
}

class DirectionalScaleSetupAction(private val scale: Float, private val direction: Direction) :
    SetupAction() {
    override fun runSetup(view: View) {
        if (direction == Direction.Right) {
            view.translationX = view.width.toFloat() * (1 - scale) / 2 * -1
        } else if (direction == Direction.Left) {
            view.translationX = view.width.toFloat() * (1 - scale) / 2
        } else if (direction == Direction.Down) {
            view.translationY = view.height.toFloat() * (1 - scale) / 2 * -1
        } else if (direction == Direction.Up) {
            view.translationY = view.height.toFloat() * (1 - scale) / 2
        }

        if (direction == Direction.Down || direction == Direction.Up) {
            view.scaleY = scale
        } else {
            view.scaleX = scale
        }
    }

    override fun getDeEffector() = DirectionalScaleSetupDeEffector(direction)
}
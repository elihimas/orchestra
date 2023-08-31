package com.elihimas.orchestra.java

import android.view.View
import com.elihimas.orchestra.animations.*
import java.util.function.Consumer

class ViewReference(vararg val views: View) : com.elihimas.orchestra.blocks.ViewReference(*views) {

    fun translate(x: Float, y: Float) = translate(x, y, null)
    fun translate(x: Float, y: Float, config: Consumer<TranslateByAnimation>) = translate(x, y, wrap(config))

    fun circularReveal() = circularReveal(null)
    fun circularReveal(config: Consumer<CircularRevealAnimation>) = circularReveal(wrap(config))

    fun slide(direction: Direction = Direction.Up) = slide(direction, null)
    fun slide(direction: Direction = Direction.Up, config: Consumer<SlideAnimation>) = slide(direction, wrap(config))

    fun scale(scale: Int) = scale(scale.toFloat(), null)
    fun scale(scale: Float) = scale(scale, scale, null)

    fun slideOut(direction: Direction = Direction.Up) = slideOut(direction, null)
    fun slideOut(direction: Direction = Direction.Up, config: Consumer<SlideAnimation>) = slideOut(direction, wrap(config))

    fun fadeOut() = fadeOut(null)

    private fun <T> wrap(config: Consumer<T>): (T.() -> Unit) {
        return { config.accept(this) }
    }
}
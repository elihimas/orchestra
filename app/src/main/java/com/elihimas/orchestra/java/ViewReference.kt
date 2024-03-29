package com.elihimas.orchestra.java

import android.view.View
import com.elihimas.orchestra.animations.CircularRevealAnimation
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.animations.slide.SlideAnimation
import com.elihimas.orchestra.animations.translations.TranslateByAnimation
import java.util.function.Consumer

class ViewReference(val views: List<View>) : com.elihimas.orchestra.blocks.ViewReference(views) {

    fun translate(x: Float, y: Float) = translateBy(x, y, null)
    fun translate(x: Float, y: Float, config: Consumer<TranslateByAnimation>) = translateBy(x, y, wrap(config))

    fun circularReveal() = circularReveal(null)
    fun circularReveal(config: Consumer<CircularRevealAnimation>) = circularReveal(wrap(config))

    fun slideIn(direction: Direction = Direction.Up) = slideIn(direction, null)
    fun slideIn(direction: Direction = Direction.Up, config: Consumer<SlideAnimation>) =
        slideIn(direction, wrap(config))

    fun scale(scale: Int) = scale(scale.toFloat(), null)
    fun scale(scale: Float) = scale(scale, scale, null)

    fun slideOut(direction: Direction = Direction.Up) = slideOut(direction, null)
    fun slideOut(direction: Direction = Direction.Up, config: Consumer<SlideAnimation>) = slideOut(direction, wrap(config))

    fun fadeOut() = fadeOut(null)

    private fun <T> wrap(config: Consumer<T>): (T.() -> Unit) {
        return { config.accept(this) }
    }
}
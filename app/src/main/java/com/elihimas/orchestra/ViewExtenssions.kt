package com.elihimas.orchestra

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

//TODO review interporlators
data class FadeOutInConfig(
        var fadeoutDuration: Long = 300,
        var fadeoutInterpolator: Interpolator = DecelerateInterpolator(),
        var inBetweenDelay: Long = 200,
        var fadeInDuration: Long = 300,
        var fadeInInterpolator: Interpolator = FastOutSlowInInterpolator()
)

fun TextView.setTextFading(text: String, createConfiguration: (FadeOutInConfig.() -> Unit) = { FadeOutInConfig() }) {
    val tvRef = this
    val configuration = FadeOutInConfig()
    createConfiguration.invoke(configuration)

    Orchestra.launch {
        on(tvRef).fadeOut {
            duration = configuration.fadeoutDuration
            interpolator = DecelerateInterpolator()
        }
    }.then {
        this.text = text

        Orchestra.launch {
            delay(configuration.inBetweenDelay)
            on(tvRef).fadeIn {
                duration = configuration.fadeoutDuration
                interpolator = configuration.fadeoutInterpolator
            }
        }
    }
}

data class ScalingConfig(
        var scaleOutDuration: Long = 300,
        var scaleIntDuration: Long = 300,
        var delayDuration: Long = 90
)

fun TextView.setTextScaling(text: String, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        this.text = text
    }
}


fun TextView.setTextScaling(resId: Int, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        this.setText(resId)
    }
}

fun ImageView.setImageResourceScaling(resId: Int, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        setImageResource(resId)
    }
}

fun ImageView.setImageDrawableScaling(drawable: Drawable, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        setImageDrawable(drawable)
    }
}

fun ImageView.setImageBitmapScaling(bitmap: Bitmap, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        setImageBitmap(bitmap)
    }
}

fun ImageView.setImageIconScaling(icon: Icon, createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }) {
    scale(createConfiguration) {
        setImageIcon(icon)
    }
}

fun View.scale(createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }, operation: () -> Unit) {
    val thisRef = this
    val configuration = ScalingConfig()
    createConfiguration.invoke(configuration)
    Orchestra.launch {
        on(thisRef).scale(0) { duration = configuration.scaleOutDuration }
    }.then {
        operation()

        Orchestra.launch {
            delay(configuration.delayDuration)
            on(thisRef).scale(1) { duration = configuration.scaleIntDuration }
        }
    }
}
package com.elihimas.orchestra

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView

//TODO review interpolators
data class FadeOutInConfig(
    var fadeoutDuration: Long = 300,
    var fadeoutInterpolator: Interpolator = AccelerateInterpolator(),
    var inBetweenDelay: Long = 0,
    var fadeInDuration: Long = 300,
    var fadeInInterpolator: Interpolator = DecelerateInterpolator()
)

fun TextView.setTextFading(
    textId: Int,
    createConfiguration: (FadeOutInConfig.() -> Unit) = { FadeOutInConfig() }
) {
    val text = context.getString(textId)
    setTextFading(text, createConfiguration)
}

fun TextView.setTextFading(
    text: String,
    createConfiguration: (FadeOutInConfig.() -> Unit) = { FadeOutInConfig() }
) {
    val tvRef = this
    val configuration = FadeOutInConfig()
    createConfiguration.invoke(configuration)

    Orchestra.launch {
        on(tvRef).fadeOut {
            duration = configuration.fadeoutDuration
            interpolator = configuration.fadeoutInterpolator
        }
    }.andThen {
        this.text = text

        Orchestra.launch {
            delay(configuration.inBetweenDelay)
            on(tvRef).fadeIn {
                duration = configuration.fadeInDuration
                interpolator = configuration.fadeInInterpolator
            }
        }
    }
}

data class ScalingConfig(
    var scaleOutDuration: Long = 300,
    val scaleOutInterpolator: Interpolator = AccelerateInterpolator(),
    var scaleInDuration: Long = 300,
    val scaleInInterpolator: Interpolator = OvershootInterpolator(1.2f),
    var inBetweenDelay: Long = 90
) {

}

fun TextView.setTextScaling(
    text: String,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        this.text = text
    }
}


fun TextView.setTextScaling(
    resId: Int,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        this.setText(resId)
    }
}

fun ImageView.setImageResourceScaling(
    resId: Int,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        setImageResource(resId)
    }
}

fun ImageView.setImageDrawableScaling(
    drawable: Drawable,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        setImageDrawable(drawable)
    }
}

fun ImageView.setImageBitmapScaling(
    bitmap: Bitmap,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        setImageBitmap(bitmap)
    }
}

fun ImageView.setImageIconScaling(
    icon: Icon,
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() }
) {
    scale(createConfiguration) {
        setImageIcon(icon)
    }
}

fun View.scale(
    createConfiguration: (ScalingConfig.() -> Unit) = { ScalingConfig() },
    operation: () -> Unit
) {
    val thisRef = this
    val configuration = ScalingConfig()
    createConfiguration.invoke(configuration)
    Orchestra.launch {
        on(thisRef).scale(0f) {
            duration = configuration.scaleOutDuration
            interpolator = configuration.scaleOutInterpolator
        }
    }.andThen {
        operation()

        Orchestra.launch {
            delay(configuration.inBetweenDelay)
            on(thisRef).scale(1f) {
                duration = configuration.scaleInDuration
                interpolator = configuration.scaleInInterpolator
            }
        }
    }
}
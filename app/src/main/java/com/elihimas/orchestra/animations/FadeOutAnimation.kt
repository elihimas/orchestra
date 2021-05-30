package com.elihimas.orchestra.animations

class FadeOutAnimation(initialAlpha: Float = 1f, finalAlpha: Float = 0f) : FadeInAnimation(initialAlpha, finalAlpha) {
    override fun clone(): Any {
        return FadeOutAnimation(initialAlpha, finalAlpha).also {
            cloneFromTo(it, this)
        }
    }
}
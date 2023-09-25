package com.elihimas.orchestra.animations.opacity

class FadeOutAnimation(initialAlpha: Float = 1f, finalAlpha: Float = 0f) : FadeInAnimation(initialAlpha, finalAlpha) {
    override fun clone(): Any {
        return FadeOutAnimation(initialAlpha, finalAlpha).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}
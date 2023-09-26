package com.elihimas.orchestra

import com.elihimas.orchestra.blocks.Block

// TODO: add delay, spacing and interpolator
data class AnimationConfiguration(var duration: Long)

interface CustomAnimationReference<T>{
    fun configureAnimation(configurationCode: AnimationConfiguration.() -> Unit): CustomAnimationRunner<T>
}

interface CustomAnimationRunner<T> {
    fun setAnimation(animationCode: (T, Float) -> Unit)
}

internal class CustomAnimationReferenceImpl<T>(
    private val views: List<T>,
) : Block(), CustomAnimationReference<T>, CustomAnimationRunner<T> {

    private val animationConfiguration: AnimationConfiguration = AnimationConfiguration(650)
    private lateinit var animationCode: (T, Float) -> Unit

    override fun setAnimation(animationCode: (T, Float) -> Unit) {
        this.animationCode = animationCode
    }

    override fun configureAnimation(configurationCode: AnimationConfiguration.() -> Unit): CustomAnimationRunner<T> {
        configurationCode(animationConfiguration)

        return this
    }

    override fun updateAnimations(time: Float) {
        val proportion = time / animationConfiguration.duration

        views.forEach { view ->
            animationCode(view, proportion)
        }
    }

    override fun checkHasForeverAnimation(): Boolean {
        return false
    }

    override fun calculateDuration(): Long {
        return animationConfiguration.duration
    }

    override fun resetForeverData() {
    }

}

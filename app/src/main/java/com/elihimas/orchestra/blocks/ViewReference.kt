package com.elihimas.orchestra.blocks

import android.view.View
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.constrains.References

open class ViewReference(private vararg val views: View) : AnimationsBlock() {

    override fun updateAnimationTimeBounds() {
        var previousAnimationStart = start
        animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime = previousAnimationStart)

            previousAnimationStart = animation.end
        }
    }

    override fun updateAnimations(time: Float) {
        doUpdateAnimations(time)
        removePastAnimations(time)
        addStartedAnimations(time)
    }

    private fun doUpdateAnimations(time: Float) {
        currentAnimations.forEach { animation ->
            updateAnimation(animation, time)
        }
    }

    //TODO review this
    override fun resetForeverData() {
        nextAnimationIndex--
        (animations[nextAnimationIndex] as ForeverAnimation).animations.nextAnimationIndex = 0
    }

    private fun removePastAnimations(time: Float) {
        while (currentAnimations.peekFirst()?.let { time >= it.end } == true) {
            val pastAnimation = currentAnimations.removeFirst()

            views.forEach { view ->
                pastAnimation.finishAnimation(view)
            }
        }
    }

    private fun updateAnimation(animation: Animation, time: Float) {
        views.forEach { view ->
            animation.updateAnimationByTime(view, time)

            view.updateAffectedViewsIfNecessary(animation)
        }
    }

    // TODO: move this to a proper location
    private fun View.updateAffectedViewsIfNecessary(animation: Animation) {
        val deEffector = animation.getDeEffector()
        val affectedViews = References.map[this]

        if (affectedViews?.views?.isNotEmpty() == true) {
            deEffector?.applyEffect(this, affectedViews)
        }
    }

    private fun addStartedAnimations(time: Float) {
        while (nextAnimationIndex < animations.size &&
            time >= animations[nextAnimationIndex].start
        ) {
            val nextAnimation = animations[nextAnimationIndex]
            nextAnimation.init(*views)

            updateAnimation(nextAnimation, time)

            currentAnimations.add(nextAnimation)
            nextAnimationIndex++
        }
    }

    override fun forever(block: AnimationsBlock.() -> Unit) {
        hasForeverAnimation = true
        ViewReference(*views).also { animations ->
            block.invoke(animations)
            val foreverAnimation = ForeverAnimation(animations)
            this.animations.add(foreverAnimation)
        }
    }

    override fun forever(animationsBlock: AnimationsBlock) =
        forever {
            addAnimations(animationsBlock)
        }
}
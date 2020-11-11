package com.elihimas.orchestra.blocks

import android.view.View
import com.elihimas.orchestra.Animations
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.coroutineDelay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

open class ViewReference(private vararg val views: View) : Animations() {
    override fun updateAnimationTimeBounds() {
        var animationStart = start
        animations.forEach { animation ->
            animation.updateAnimationTimeBounds(baseTime = animationStart)

            animationStart = animation.end
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

    private fun removePastAnimations(time: Float) {
        while (currentAnimations.peekFirst()?.let { time >= it.end } == true) {
            val pastAnimation = currentAnimations.removeFirst()

            views.forEach { view ->
                pastAnimation.updateAnimationByProportion(view, 1f)
            }
        }
    }

    private fun updateAnimation(animation: Animation, time: Float) {
        views.forEach { view ->
            animation.updateAnimationByTime(view, time)
        }
    }

    private fun addStartedAnimations(time: Float) {
        while (nextAnimationIndex < animations.size &&
                time >= animations[nextAnimationIndex].start) {
            val nextAnimation = animations[nextAnimationIndex]
            nextAnimation.init(*views)

            updateAnimation(nextAnimation, time)

            currentAnimations.add(nextAnimation)
            nextAnimationIndex++
        }
    }

    override suspend fun runBlock(orchestra: Orchestra) {
        animations.forEach { action ->
            val latch = CountDownLatch(views.size)
            if (views.size > 1) {
                views.forEach { view ->
                    GlobalScope.launch(Dispatchers.Main) {
                        action.runAnimation(view) { latch.countDown() }
                    }

                    coroutineDelay(action.spacing)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    views.forEach { view ->
                        action.runAnimation(view) { latch.countDown() }
                    }
                }
            }
            latch.await()
        }
    }
}
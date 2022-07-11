package com.elihimas.orchestra.blocks

import com.elihimas.orchestra.Orchestra
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class ParallelBlock(private val orchestraContext: Orchestra) : Block() {

    override fun checkHasForeverAnimation(): Boolean {
		// TODO: review this
        return orchestraContext.blocks.any { it.checkHasForeverAnimation() }
    }

    override fun updateAnimationTimeBounds() {
        orchestraContext.blocks.forEach { block ->
            block.start = start
            block.end = end

            block.updateAnimationTimeBounds()
        }

        start = orchestraContext.blocks.minOf { block -> block.start }
        end = orchestraContext.blocks.maxOf { block -> block.end }
    }

    override fun updateAnimations(time: Float) {
        orchestraContext.blocks.forEach { block ->
            block.updateAnimations(time)
        }
    }

    override fun resetForeverData() {
		// TODO: review this
        orchestraContext.blocks.forEach { block ->
            block.resetForeverData()
        }
        updateAnimationTimeBounds()
    }

    override fun calculateDuration() =
        orchestraContext.blocks.maxOf { block -> block.calculateDuration() }
}
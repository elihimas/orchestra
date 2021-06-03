package com.elihimas.orchestra.blocks

import com.elihimas.orchestra.Orchestra
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class ParallelBlock(private val orchestraContext: Orchestra) : Block() {
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

    override suspend fun runBlock(orchestra: Orchestra) {
        orchestraContext.blocks.let { blocks ->
            val parallelLatch = CountDownLatch(blocks.size)

            orchestraContext.blocks.forEach { block ->
                GlobalScope.launch {
                    orchestraContext.doRunBlocks(listOf(block), parallelLatch)
                }
            }

            parallelLatch.await()
        }
    }

    override fun calculateDuration() = orchestraContext.blocks.maxOf { block -> block.calculateDuration() }
}
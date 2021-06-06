package com.elihimas.orchestra

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.elihimas.orchestra.blocks.Block
import java.util.*
import kotlin.math.max

private fun LinkedList<Block>.removeStartedBlocks(time: Float): LinkedList<Block> {
    val removedBlocks = LinkedList<Block>()

    while (this.peekFirst()?.let { time >= it.start } == true) {
        this.removeFirst().let {
            removedBlocks.addLast(it)
            it.runBlock()
        }
    }

    return removedBlocks
}

class AnimationTicker {

    private var currentTime = 0f

    //TODO synchronize
    private lateinit var nextBlocks: LinkedList<Block>
    private val foreverBlocks = mutableSetOf<Block>()

    //TODO should be a list of blocks
    private var currentBlocks = LinkedList<Block>()

    private val updateListener: ValueAnimator.AnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        update(it.animatedValue as Float)
    }

    private fun update(time: Float) {
        currentTime = time

        updateBlocks(time)
        removePastBlocks(time)
        addStartedBlocks(time)
    }

    private fun updateBlocks(time: Float) {
        currentBlocks.forEach {
            it.updateAnimations(time)
        }
    }

    private fun removePastBlocks(time: Float) {
        while (currentBlocks.peekFirst()?.let { time >= it.end } == true) {
            currentBlocks.removeFirst()
        }
    }

    private fun updateInfiniteBlock(removedBlock: Block, time: Float) {
        val blockDuration = removedBlock.calculateDuration()

        removedBlock.start = time
        removedBlock.end = removedBlock.start + blockDuration
        removedBlock.updateAnimationTimeBounds()
        removedBlock.resetForeverData()
    }

    private fun addStartedBlocks(time: Float) {
        nextBlocks.removeStartedBlocks(time).let {
            if (it.isNotEmpty()) {
                currentBlocks.addAll(it)
            }
        }
    }

    fun start(blocks: LinkedList<Block>, forceAnimationStart: Boolean = false) {
        val baseTime = currentTime

        nextBlocks = blocks
        val blocksEndTime = updateBlocksTimeBoundsAndCalculateEnd(nextBlocks, currentBlocks, baseTime)

        addForeverBlocks(blocks)
        val started = nextBlocks.removeStartedBlocks(baseTime)
        currentBlocks.addAll(started)

        if (baseTime == 0f || forceAnimationStart) {
            ValueAnimator.ofFloat(baseTime, blocksEndTime).apply {
                duration = (blocksEndTime - baseTime).toLong()
                interpolator = LinearInterpolator()

                addUpdateListener(updateListener)
                doOnEnd { doOnEnd() }
            }.start()
        }
    }

    private fun addForeverBlocks(blocks: LinkedList<Block>) {
        val foreverBlocks = blocks.filter { it.hasForeverAnimation }
        this.foreverBlocks.addAll(foreverBlocks)
    }

    private fun doOnEnd() {
        if (foreverBlocks.isNotEmpty()) {
            foreverBlocks.forEach {
                updateInfiniteBlock(it, currentTime)
            }
            nextBlocks.addAll(foreverBlocks)
        }
        if (nextBlocks.isNotEmpty() || currentBlocks.isNotEmpty()) {
            start(nextBlocks, forceAnimationStart = true)
        } else {
            Orchestra.disposeCurrentOrchestra()
        }
    }

    private fun updateBlocksTimeBoundsAndCalculateEnd(newBlocks: LinkedList<Block>,
                                                      currentBlocks: LinkedList<Block>,
                                                      baseTime: Float): Float {
        var blocksEnd = baseTime

        newBlocks.forEach { block ->
            val blockDuration = block.calculateDuration()

            block.start = blocksEnd
            block.end = block.start + blockDuration
            block.updateAnimationTimeBounds()

            blocksEnd += blockDuration
        }


        val currentBlocksMaxEnd = currentBlocks.maxOfOrNull { it.end } ?: 0f

        return max(blocksEnd, currentBlocksMaxEnd)
    }
}
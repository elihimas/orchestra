package com.elihimas.orchestra.blocks

import com.elihimas.orchestra.Orchestra

abstract class Block {
    internal var start = 0F
    var end = 0F

    abstract suspend fun runBlock(orchestra: Orchestra)

    abstract fun calculateDuration(): Long
    open fun updateAnimations(time: Float) {}
    open fun updateAnimationTimeBounds() {}
}
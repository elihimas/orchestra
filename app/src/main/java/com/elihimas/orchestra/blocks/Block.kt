package com.elihimas.orchestra.blocks

import com.elihimas.orchestra.Orchestra

abstract class Block {
    internal var start = 0f
    var end = 0f
    var hasForeverAnimation = false

    open fun runBlock() {}

    abstract fun calculateDuration(): Long
    open fun updateAnimations(time: Float) {}
    open fun updateAnimationTimeBounds() {}

    open fun resetForeverData() {}
}
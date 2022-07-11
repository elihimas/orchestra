package com.elihimas.orchestra.blocks

abstract class Block {
    internal var start = 0f
    var end = 0f
    var hasForeverAnimation = false

    abstract fun checkHasForeverAnimation(): Boolean

    open fun runBlock() {}

    abstract fun calculateDuration(): Long
    open fun updateAnimations(time: Float) {}
    open fun updateAnimationTimeBounds() {}

    abstract fun resetForeverData()
}
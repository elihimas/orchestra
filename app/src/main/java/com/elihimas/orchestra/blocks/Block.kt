package com.elihimas.orchestra.blocks

abstract class Block {
    internal var start = 0f
    internal var end = 0f
    internal var hasForeverAnimation = false

    internal open val viewsCount: Int
        get() = TODO()

    internal abstract fun checkHasForeverAnimation(): Boolean

    internal open fun runBlock() {}

    internal abstract fun calculateDuration(): Long
    internal open fun updateAnimations(time: Float) {}
    internal open fun updateAnimationTimeBounds() {}

    internal abstract fun resetForeverData()
}
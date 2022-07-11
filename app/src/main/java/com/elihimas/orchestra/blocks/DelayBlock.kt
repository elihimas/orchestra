package com.elihimas.orchestra.blocks

class DelayBlock(val duration: Long) : Block() {

    override fun resetForeverData() {
		// TODO: review this
    }
    override fun calculateDuration() = duration

    override fun checkHasForeverAnimation(): Boolean = false
}
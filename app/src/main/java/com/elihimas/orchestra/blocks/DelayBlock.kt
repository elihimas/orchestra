package com.elihimas.orchestra.blocks

class DelayBlock(val duration: Long) : Block() {

    override fun calculateDuration() = duration
}
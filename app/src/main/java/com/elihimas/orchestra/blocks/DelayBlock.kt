package com.elihimas.orchestra.blocks

import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.coroutineDelay

class DelayBlock(val duration: Long) : Block() {
    override suspend fun runBlock(orchestra: Orchestra) {
        coroutineDelay(duration)
    }

    override fun calculateDuration() = duration
}
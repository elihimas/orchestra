package com.elihimas.orchestra.java

import android.view.View
import com.elihimas.orchestra.SetupContext
import com.elihimas.orchestra.blocks.ParallelBlock
import java.util.function.Consumer

class Orchestra : com.elihimas.orchestra.Orchestra() {

    override fun on(vararg views: View): ViewReference = ViewReference(*views).apply {
        blocks.add(this)
    }

    fun parallel(block: Consumer<Orchestra>): Orchestra {
        return parallel {
            block.accept(this as Orchestra)
        } as Orchestra
    }

    fun then(block: Runnable) {
        then {
            block.run()
        }
    }

    companion object {

        @JvmStatic
        fun setup(block: Consumer<SetupContext>): SetupContext {
            val setupContext = SetupContext()
            block.accept(setupContext)
            setupContext.runSetup()

            return setupContext
        }

        @JvmStatic
        fun launch(block: Consumer<Orchestra>): Orchestra {
            createOrchestra = { Orchestra() }

            val orchestraContext = currentOrchestra() as Orchestra
            block.accept(orchestraContext)
            orchestraContext.runBlocks()

            return orchestraContext
        }
    }
}
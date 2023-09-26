package com.elihimas.orchestra

//TODO: add suppport to read configuration from xml
object OrchestraConfiguration {
    object General {
        var timeSpacing = 0L
        var duration = 2600L
    }

    object AnimateAndWaitDuration {
        var animationDuration = 1000L
        var waitDuration = 0L
    }

    object Bounce {
        var startDelay = 1500L
        var height = R.dimen.bounce_height
    }
}
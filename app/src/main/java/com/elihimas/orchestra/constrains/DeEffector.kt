package com.elihimas.orchestra.constrains

import android.view.View

interface DeEffector {
    fun beforeAnimation(source: View, affectedViews: AffectedViews) {}
    fun applyEffect(source: View, affectedViews: AffectedViews)
}

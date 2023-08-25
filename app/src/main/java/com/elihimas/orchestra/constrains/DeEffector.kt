package com.elihimas.orchestra.constrains

import android.view.View

interface DeEffector {
    fun applyEffect(source: View, affectedViews: AffectedViews)
}

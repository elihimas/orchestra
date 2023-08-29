package com.elihimas.orchestra.constrains

import android.view.View
import java.util.WeakHashMap

object References {
    // TODO: since one reference view can be referenced by many other views, we should use
    //  a list of affected views, not a single element
    val map = WeakHashMap<View, AffectedViews>()
}

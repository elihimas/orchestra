package com.elihimas.orchestra.animations

import android.view.View
import androidx.annotation.ColorRes

class ChangeBackgroundAnimation(@ColorRes initialColorRes: Int, @ColorRes finalColorRes: Int) : ColorAnimation(initialColorRes, finalColorRes) {

    override fun clone(): Any {
        return ChangeBackgroundAnimation(initialColor, finalColor).also { clone ->
            cloneFromTo(this, clone)
        }
    }

    override fun update(view: View, color: Int) {
        view.setBackgroundColor(color)
    }
}
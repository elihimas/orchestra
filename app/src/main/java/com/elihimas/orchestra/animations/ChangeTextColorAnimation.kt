package com.elihimas.orchestra.animations

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes

class ChangeTextColorAnimation(@ColorRes initialColorRes: Int, @ColorRes finalColorRes: Int) : ColorAnimation(initialColorRes, finalColorRes) {

    override fun clone(): Any {
        return ChangeTextColorAnimation(initialColor, finalColor).also {
            cloneFromTo(it, this)
        }
    }

    override fun update(view: View, color: Int) {
        (view as TextView?)?.setTextColor(color)
    }
}
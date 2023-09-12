package com.elihimas.orchestra.references

import android.graphics.PointF
import android.view.View

fun leftOf(target: View): Reference {
    return Reference(target, ReferenceType.leftOf)
}

enum class ReferenceType {
    leftOf, rightOf,bellow, above
}

data class Reference(val target: View, val type: ReferenceType) {

}
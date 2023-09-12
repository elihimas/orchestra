package com.elihimas.orchestra.animations.translations

import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.references.Reference

class TranslateToReferenceAnimation(private val reference: Reference) :
    Animation() {

    override fun clone(): Any {
        return TranslateToReferenceAnimation(reference).also { clone ->
            cloneFromTo(this, clone)
        }
    }
}
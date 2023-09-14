package com.elihimas.orchestra.references

import android.view.View

fun startToStartOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.startToStartOf)
}

fun startToEndOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.startToEndOf)
}

fun endToStartOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.endToStartOf)
}

fun endToEndOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.endToEndOf)
}

fun topToTopOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.topToTopOf)
}

fun topToBottomOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.topToBottomOf)
}

fun bottomToTopOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.bottomTotopOf)
}

fun bottomToBottomOf(target: View): TranslationReference {
    return SingleTranslationReference(target, ReferenceType.bottomToBottom)
}

enum class ReferenceType {
    startToStartOf, startToEndOf, endToStartOf, endToEndOf,
    topToTopOf, topToBottomOf, bottomTotopOf, bottomToBottom,
}

interface TranslationReference {
    operator fun plus(reference: TranslationReference): TranslationReference
    fun getPointFor(view: View): TranslationTarget
}

data class SingleTranslationReference(val target: View, val type: ReferenceType) :
    TranslationReference {
    override fun plus(reference: TranslationReference): TranslationReference =
        DoubleTranslationReference(this, reference)

    override fun getPointFor(view: View): TranslationTarget {
        var x: Float? = null
        var y: Float? = null

        when (type) {
            ReferenceType.startToStartOf -> {
                x = target.x
            }

            ReferenceType.startToEndOf -> {
                x = target.x + target.width
            }

            ReferenceType.endToStartOf -> {
                x = target.x - view.width
            }

            ReferenceType.endToEndOf -> {
                x = target.x + target.width - view.width
            }

            ReferenceType.topToTopOf -> {
                y = target.y
            }

            ReferenceType.topToBottomOf -> {
                y = target.y + target.height
            }

            ReferenceType.bottomTotopOf -> {
                y = target.y - view.height
            }

            ReferenceType.bottomToBottom -> {
                y = target.y + target.height - view.height
            }
        }

        return TranslationTarget(x, y)
    }
}

data class DoubleTranslationReference(
    val firstReference: TranslationReference,
    val secondReference: TranslationReference
) :
    TranslationReference {
    override fun plus(reference: TranslationReference): TranslationReference =
        DoubleTranslationReference(this, reference)

    override fun getPointFor(view: View): TranslationTarget {
        val firstReferenceTarget = firstReference.getPointFor(view)
        val secondReferenceTarget = secondReference.getPointFor(view)

        return firstReferenceTarget + secondReferenceTarget
    }
}

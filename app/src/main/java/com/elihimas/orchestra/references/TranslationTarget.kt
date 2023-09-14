package com.elihimas.orchestra.references

data class TranslationTarget(val destinationX: Float?, val destinationY: Float?) {
    operator fun plus(otherTranslationTarget: TranslationTarget): TranslationTarget {
        val compoundX = otherTranslationTarget.destinationX ?: destinationX
        val compoundY = otherTranslationTarget.destinationY ?: destinationY

        return TranslationTarget(compoundX, compoundY)
    }
}
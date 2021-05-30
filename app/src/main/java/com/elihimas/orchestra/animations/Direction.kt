package com.elihimas.orchestra.animations

enum class Direction {
    Up, Down, Left, Right;

    fun reverse(): Direction =
            when (this) {
                Up -> Down
                Down -> Up
                Left -> Right
                Right -> Left
            }
}
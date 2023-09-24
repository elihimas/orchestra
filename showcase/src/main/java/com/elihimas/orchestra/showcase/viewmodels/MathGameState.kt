package com.elihimas.orchestra.showcase.viewmodels

data class MathGameState(
    val score: Int,
    val gameQuestion: GameQuestion
)

data class GameQuestion(
    val statement: String,
    val options: List<String>,
    val expectedAnswer: String
)
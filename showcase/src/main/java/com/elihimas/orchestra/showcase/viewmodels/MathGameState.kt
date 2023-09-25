package com.elihimas.orchestra.showcase.viewmodels

data class MathGameState(
    val score: Int,
    val gameQuestion: GameQuestion
)

data class GameQuestion(
    val statement: String,
    val options: List<QuestionOption>,
    val expectedAnswer: String
)

data class QuestionOption(val text: String, val isAvailable: Boolean)
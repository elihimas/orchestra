package com.elihimas.orchestra.showcase.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MathGameViewModel : ViewModel() {

    val state: MutableStateFlow<MathGameState> = MutableStateFlow(createInitialState())

    private fun createInitialState() = MathGameState(0, createQuestion())

    fun onUserAnswered(answer: String) {
        state.update { previousState ->
            val isRightAnswer = previousState.gameQuestion.expectedAnswer == answer
            val newScore = if (isRightAnswer) {
                previousState.score + 1
            } else {
                previousState.score
            }

            previousState.copy(score = newScore, gameQuestion = createQuestion())
        }
    }

    private fun createQuestion(): GameQuestion {
        val n1 = (3..10).random()
        val n2 = (3..10).random()

        val answer = n1 + n2
        val wrongAnswers = listOf(
            answer + (1..3).random(),
            answer - (1..3).random(),
            answer + (3..4).random()
        )

        val allAnswers = wrongAnswers + answer

        return GameQuestion(
            "$n1 + $n2?",
            allAnswers.shuffled().map(Int::toString),
            answer.toString()
        )
    }
}

package com.elihimas.orchestra.showcase.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MathGameViewModel : ViewModel() {

    val state: MutableStateFlow<MathGameState> = MutableStateFlow(createInitialState())
    val messages = MutableSharedFlow<MathGameMessage>()

    private fun createInitialState() = MathGameState(0, createQuestion())

    fun onUserAnswered(answer: String) {
        state.update { previousState ->
            val isRightAnswer = previousState.gameQuestion.expectedAnswer == answer
            val gameQuestion: GameQuestion
            val newScore: Int

            if (isRightAnswer) {
                newScore = previousState.score + 1
                gameQuestion = createQuestion()
            } else {

                val updatedOptions = previousState.gameQuestion.options.map {
                    if(it.text == answer){
                        QuestionOption(it.text, false)
                    }else{
                        it
                    }
                }

                viewModelScope.launch {
                }

                newScore = previousState.score
                gameQuestion = previousState.gameQuestion.copy(options = updatedOptions)
            }

            previousState.copy(score = newScore, gameQuestion = gameQuestion)
        }
    }

    private fun createQuestion(): GameQuestion {
        val n1 = (3..10).random()
        val n2 = (3..10).random()

        val answer = n1 + n2
        val wrongAnswers = listOf(
            answer + (1..3).random(),
            answer - (1..3).random(),
            answer + (4..5).random()
        )

        val allAnswers = wrongAnswers + answer

        return GameQuestion(
            "$n1 + $n2?",
            allAnswers.shuffled().map { QuestionOption(it.toString(), true) },
            answer.toString()
        )
    }
}

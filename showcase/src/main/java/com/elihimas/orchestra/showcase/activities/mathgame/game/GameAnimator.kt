package com.elihimas.orchestra.showcase.activities.mathgame.game

import android.animation.ArgbEvaluator
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivityMathGameBinding
import com.elihimas.orchestra.showcase.viewmodels.QuestionOption

class GameAnimator(private val binding: ActivityMathGameBinding) {

    private var currentStatementText: TextView? = null
    private val colorEvaluator = ArgbEvaluator()

    fun init() = with(binding) {
        Orchestra.setup {
            on(tvStatement1, tvStatement2).setSlideInTo(
                visibleSize = 0f, direction = Direction.Up
            )
        }
    }

    fun renderScore(score: Int) = with(binding) {
        tvScore.text = score.toString()
    }

    fun renderStatement(statement: String) = with(binding) {
        val current = currentStatementText ?: tvStatement1
        val next = if (current == tvStatement1) {
            tvStatement2
        } else {
            tvStatement1
        }

        if (current.text == statement) {
            return
        }

        next.text = statement

        Orchestra.launch {
            parallel {
                on(current).slideOut(Direction.Down) {
                    duration = 400
                }

                on(next).slideIn(Direction.Up) {
                    duration = 400
                    delay = 80
                }
            }
        }.andThen {
            currentStatementText = next
        }
    }

    fun renderOptions(options: List<QuestionOption>) = with(binding) {
        val optionsCards = listOf(btAnswerCard1, btAnswerCard2, btAnswerCard3, btAnswerCard4)
        val optionsTexts = listOf(tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4)
        val cardsOptionsAndTexts = optionsCards.zip(options).zip(optionsTexts)

        Orchestra.launch {
            parallel {
                cardsOptionsAndTexts.forEach { cardAndOption ->
                    val card = cardAndOption.first.first
                    val option = cardAndOption.first.second
                    val text = cardAndOption.second

                    card.isEnabled = option.isAvailable

                    updateOpacity(this, card, option)
                    updateBackground(this, card)
                    updateTextColor(this, text, option)
                }
            }
        }
    }

    private fun updateOpacity(
        orchestraContext: OrchestraContext,
        card: CardView,
        option: QuestionOption
    ) = with(orchestraContext) {
        val alpha = if (option.isAvailable) {
            1f
        } else {
            0.8f
        }

        on(card).opacityTo(alpha) {
            duration = 400
        }
    }

    private fun updateBackground(orchestraContext: OrchestraContext, card: CardView) =
        with(orchestraContext) {
            val context = card.context
            val initialColor = card.cardBackgroundColor.defaultColor
            val finalColor = if (card.isEnabled) {
                context.getColor(R.color.white)
            } else {
                context.getColor(R.color.red)
            }

            createCustomAnimationFor(card)
                .configureAnimation {
                    duration = 650
                }.setAnimation { card, animationProportion ->
                    val animatedColor = colorEvaluator.evaluate(
                        animationProportion,
                        initialColor,
                        finalColor
                    ) as Int

                    card.setCardBackgroundColor(animatedColor)
                }
        }


    private fun updateTextColor(
        orchestraContext: OrchestraContext,
        textView: TextView,
        option: QuestionOption
    ) = with(orchestraContext) {
        val context = textView.context
        val initialColor = textView.currentTextColor
        val targetColor = if (option.isAvailable) {
            context.getColor(R.color.black)
        } else {
            context.getColor(R.color.white)
        }

        this.createCustomAnimationFor(textView)
            .configureAnimation { duration = 600 }
            .setAnimation { textView, animationProportion ->
                val animatedColor = colorEvaluator.evaluate(
                    animationProportion,
                    initialColor,
                    targetColor
                ) as Int

                textView.setTextColor(animatedColor)
            }
    }
}
package com.elihimas.orchestra.showcase.activities.mathgame

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.ActivityMathGameBinding
import com.elihimas.orchestra.showcase.viewmodels.MathGameMessage
import com.elihimas.orchestra.showcase.viewmodels.MathGameState
import com.elihimas.orchestra.showcase.viewmodels.MathGameViewModel
import com.elihimas.orchestra.showcase.viewmodels.QuestionOption
import kotlinx.coroutines.launch

class MathGameActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMathGameBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MathGameViewModel>()
    private val colorEvaluator = ArgbEvaluator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initViews()

        initOrchestra()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect(::renderState)
        }
        lifecycleScope.launch {
            viewModel.messages.collect(::handleMessage)
        }
    }

    private fun initViews() = with(binding) {
        val answerListener = View.OnClickListener { source ->
            val answser = (source as TextView).text.toString()

            viewModel.onUserAnswered(answser)
        }

        tvAnswer1.setOnClickListener(answerListener)
        tvAnswer2.setOnClickListener(answerListener)
        tvAnswer3.setOnClickListener(answerListener)
        tvAnswer4.setOnClickListener(answerListener)
    }

    private fun handleMessage(message: MathGameMessage) {

    }


    private fun renderState(state: MathGameState) = with(binding) {
        renderScore(state.score)
        renderStatement(state.gameQuestion.statement)
        renderOptionsTexts(state.gameQuestion.options)
        renderOptionsAnimations(state.gameQuestion.options)
    }

    private fun renderScore(score: Int) = with(binding) {
        tvScore.text = score.toString()
    }

    private fun renderStatement(statement: String) {
        setStatement(statement)
    }

    private fun renderOptionsTexts(options: List<QuestionOption>) = with(binding) {
        tvAnswer1.text = options[0].text
        tvAnswer2.text = options[1].text
        tvAnswer3.text = options[2].text
        tvAnswer4.text = options[3].text
    }

    private fun renderOptionsAnimations(options: List<QuestionOption>) = with(binding) {
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

                    changeOpacity(this, card, option)
                    updateBackground(this, card)
                    updateText(this, text, option)
                }
            }
        }
    }

    private fun changeOpacity(
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
            val initialColor = card.cardBackgroundColor.defaultColor
            val cardColor = if (card.isEnabled) {
                getColor(R.color.white)
            } else {
                getColor(R.color.red)
            }

            createCustomAnimationFor(card)
                .configureAnimation {
                    duration = 650
                }.setAnimation { card, animationProportion ->
                    val animatedColor = colorEvaluator.evaluate(
                        animationProportion,
                        initialColor,
                        cardColor
                    ) as Int

                    card.setCardBackgroundColor(animatedColor)
                }
        }

    private fun updateText(
        orchestraContext: OrchestraContext,
        textView: TextView,
        option: QuestionOption
    ) = with(orchestraContext) {
        val initialColor = textView.currentTextColor
        val targetColor = if (option.isAvailable) {
            getColor(R.color.black)
        }else{
            getColor(R.color.white)
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

    private var currentStatementText: TextView? = null
    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(tvStatement1, tvStatement2).setSlideInTo(
                visibleSize = 0f, direction = Direction.Up
            )
        }
    }

    private fun setStatement(statement: String) = with(binding) {
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
}
package com.elihimas.orchestra.showcase.activities.mathgame

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.showcase.databinding.ActivityMathGameBinding
import com.elihimas.orchestra.showcase.viewmodels.MathGameState
import com.elihimas.orchestra.showcase.viewmodels.MathGameViewModel
import kotlinx.coroutines.launch

class MathGameActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMathGameBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MathGameViewModel>()

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

    private fun renderState(state: MathGameState) = with(binding) {
        tvScore.text = state.score.toString()

        setStatement(state.gameQuestion.statement)

        tvAnswer1.text = state.gameQuestion.options[0]
        tvAnswer2.text = state.gameQuestion.options[1]
        tvAnswer3.text = state.gameQuestion.options[2]
        tvAnswer4.text = state.gameQuestion.options[3]
    }

    private var curentStatemntText: TextView? = null

    private fun initOrchestra() = with(binding) {
        Orchestra.setup {
            on(tvStatement1, tvStatement2).setSlideInTo(
                visibleSize = 0f, direction = Direction.Up
            )
        }
    }

    private fun setStatement(statement: String) = with(binding) {
        val current = curentStatemntText ?: tvStatement1
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
            curentStatemntText = next
        }

    }
}
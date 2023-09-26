package com.elihimas.orchestra.showcase.activities.mathgame.game

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.elihimas.orchestra.showcase.databinding.ActivityMathGameBinding
import com.elihimas.orchestra.showcase.viewmodels.MathGameMessage
import com.elihimas.orchestra.showcase.viewmodels.MathGameState
import com.elihimas.orchestra.showcase.viewmodels.MathGameViewModel
import com.elihimas.orchestra.showcase.viewmodels.QuestionOption
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMathGameBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MathGameViewModel>()
    private val animator by lazy { GameAnimator(binding) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initViews()

        animator.init()
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
            val answer = (source as TextView).text.toString()

            viewModel.onUserAnswered(answer)
        }

        tvAnswer1.setOnClickListener(answerListener)
        tvAnswer2.setOnClickListener(answerListener)
        tvAnswer3.setOnClickListener(answerListener)
        tvAnswer4.setOnClickListener(answerListener)
    }

    private fun handleMessage(message: MathGameMessage) {

    }

    private fun renderState(state: MathGameState) = with(binding) {
        with(animator) {
            renderScore(state.score)
            renderStatement(state.gameQuestion.statement)
            renderOptions(state.gameQuestion.options)
        }

        renderOptionsTexts(state.gameQuestion.options)
    }

    private fun renderOptionsTexts(options: List<QuestionOption>) = with(binding) {
        tvAnswer1.text = options[0].text
        tvAnswer2.text = options[1].text
        tvAnswer3.text = options[2].text
        tvAnswer4.text = options[3].text
    }
}
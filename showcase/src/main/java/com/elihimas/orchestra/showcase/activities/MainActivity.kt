package com.elihimas.orchestra.showcase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.adapters.ExamplesAdapter
import com.elihimas.orchestra.showcase.databinding.ActivityMainBinding
import com.elihimas.orchestra.showcase.examples.Examples

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        val examples = Examples.values().toList()
        examplesRecycler.adapter = ExamplesAdapter(examples, ::onExampleSelected, ::exampleToName)
    }

    private fun onExampleSelected(example: Examples) {
        val selectedActivity = when (example) {
            Examples.SlidingLogin -> SlidingLoginActivity::class.java
            Examples.ScalingLogin -> ScalingLoginActivity::class.java
            Examples.AnimatedButton -> AnimatedButtonActivity::class.java
            Examples.Translation -> TranslateActivity::class.java
            Examples.Escale -> ScaleActivity::class.java
        }

        startActivity(Intent(this, selectedActivity))
    }

    private fun exampleToName(example: Examples): String {
        val exampleStringId = when (example) {
            Examples.SlidingLogin -> R.string.example_sliding_login
            Examples.ScalingLogin -> R.string.example_scaling_login
            Examples.AnimatedButton -> R.string.example_animated_button
            Examples.Translation -> R.string.example_translation
            Examples.Escale -> R.string.example_scale
        }

        return getString(exampleStringId)
    }
}
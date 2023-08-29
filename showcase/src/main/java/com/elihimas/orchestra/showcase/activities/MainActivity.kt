package com.elihimas.orchestra.showcase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.showcase.ButtonExample1Activity
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.adapters.ExamplesAdapter
import com.elihimas.orchestra.showcase.databinding.ActivityMainBinding
import com.elihimas.orchestra.showcase.examples.Examples

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() = with(binding) {
        val examples = Examples.values().toList()
        examplesRecycler.adapter = ExamplesAdapter(examples, ::onExampleSelected, ::exampleToName)
    }

    private fun onExampleSelected(example: Examples) {
        val selectedActivity = when (example) {
            Examples.LoginExample1 -> LoginExample1Activity::class.java
            Examples.LoginExample2 -> LoginExample2Activity::class.java
            Examples.ButtonExample1 -> ButtonExample1Activity::class.java
            Examples.Translation -> TranslateActivity::class.java
            Examples.Escale -> ScaleActivity::class.java
        }

        startActivity(Intent(this, selectedActivity))
    }

    private fun exampleToName(example: Examples): String {
        val exampleStringId = when (example) {
            Examples.LoginExample1 -> R.string.example_login1
            Examples.LoginExample2 -> R.string.example_login2
            Examples.ButtonExample1 -> R.string.example_button1
            Examples.Translation -> R.string.example_translation
            Examples.Escale -> R.string.example_scale
        }

        return getString(exampleStringId)
    }
}
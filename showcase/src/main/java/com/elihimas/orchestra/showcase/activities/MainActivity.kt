package com.elihimas.orchestra.showcase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.showcase.ButtonExample1Activity
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
            Examples.Example -> TestActivity::class.java
        }

        startActivity(Intent(this, selectedActivity))
    }

    private fun exampleToName(example: Examples): String {
        return when (example) {
            Examples.LoginExample1 -> "Login 1"
            Examples.LoginExample2 -> "Login 2"
            Examples.ButtonExample1 -> "Button 1"
            Examples.Example -> "Test"
        }
    }
}
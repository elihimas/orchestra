package com.elihimas.orchestra.showcase.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.showcase.adapters.ExamplesAdapter
import com.elihimas.orchestra.showcase.databinding.ActivityMainBinding
import com.elihimas.orchestra.showcase.examples.Example

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        val examples = Example.getExamples()
        examplesRecycler.adapter = ExamplesAdapter(examples, ::onExampleSelected)
    }

    private fun onExampleSelected(example: Example) {
        startActivity(Intent(this, example.clazz))
    }

}
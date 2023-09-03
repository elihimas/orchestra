package com.elihimas.orchestra.examples.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elihimas.orchestra.adapter.Example
import com.elihimas.orchestra.adapter.ExamplesAdapter
import com.elihimas.orchestra.examples.Examples
import com.elihimas.orchestra.examples.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.rvExamples.adapter = ExamplesAdapter(Examples.getExamples(), ::openExample)
    }

    private fun openExample(example: Example) {
        startActivity(Intent(this, example.clazz))
    }
}
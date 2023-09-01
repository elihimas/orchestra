package com.elihimas.orchestra.showcase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.showcase.databinding.RowExampleBinding
import com.elihimas.orchestra.showcase.examples.Example

class ExamplesAdapter(
    private val examples: List<Example>,
    private val onExampleSelected: (Example) -> Unit
) :
    RecyclerView.Adapter<ExamplesAdapter.ExampleHolder>() {

    class ExampleHolder(val binding: RowExampleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowExampleBinding.inflate(inflater, parent, false).apply {
            root.setOnClickListener { root ->
                onExampleSelected(root.tag as Example)
            }
        }

        return ExampleHolder(binding)
    }

    override fun getItemCount() = examples.size

    override fun onBindViewHolder(holder: ExampleHolder, position: Int) {
        val example = examples[position]

        with(holder.binding) {
            root.tag = example
            tvName.text = root.context.getString(example.exampleStringId)
        }
    }
}

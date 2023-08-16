package com.elihimas.orchestra.activities.adapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.databinding.OptionRowBinding
import com.elihimas.orchestra.model.Examples
import com.elihimas.orchestra.model.ResourcesMapper

class ClickListener(private val itemSelectedListener: ItemSelectedListener) : View.OnClickListener {

    override fun onClick(clickSource: View) {
        val example = clickSource.tag as Examples
        itemSelectedListener.itemSelected(example)
    }
}

interface ItemSelectedListener {
    fun itemSelected(examples: Examples)
}

class ExamplesAdapter(itemSelectedListener: ItemSelectedListener) :
    RecyclerView.Adapter<ExamplesAdapter.ExamplesHolder>() {

    inner class ExamplesHolder(val binding: OptionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.nameText.setOnClickListener(listener)
        }
    }

    private val listener = ClickListener(itemSelectedListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExamplesHolder(OptionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = Examples.values().size

    override fun onBindViewHolder(holder: ExamplesHolder, position: Int) = with(holder.binding) {
        val example = Examples.values()[position]

        nameText.tag = example
        nameText.setText(ResourcesMapper.map(example))
    }

}

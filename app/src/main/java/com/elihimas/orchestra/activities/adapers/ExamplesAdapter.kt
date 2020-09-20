package com.elihimas.orchestra.activities.adapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.R
import com.elihimas.orchestra.model.Examples
import com.elihimas.orchestra.model.ResourcesMapper
import kotlinx.android.synthetic.main.option_row.view.*

class ClickListener(private val recyclerView: RecyclerView, private val itemSelectedListener: ItemSelectedListener) : View.OnClickListener {

    override fun onClick(clickSource: View) {
        val position: Int = recyclerView.getChildLayoutPosition(clickSource)
        itemSelectedListener.itemSelected(Examples.values()[position])
    }
}

interface ItemSelectedListener {
    fun itemSelected(examples: Examples)
}

class ExamplesAdapter(recyclerView: RecyclerView, itemSelectedListener: ItemSelectedListener) : RecyclerView.Adapter<ExamplesAdapter.ExamplesHolder>() {

    private val listener = ClickListener(recyclerView, itemSelectedListener)

    class ExamplesHolder(view: View, val nameText: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater.from(parent.context).inflate(R.layout.option_row, parent, false).let {
                it.setOnClickListener(listener)
                ExamplesHolder(it, it.nameText)
            }

    override fun getItemCount() = Examples.values().size

    override fun onBindViewHolder(holder: ExamplesHolder, position: Int) {
        val example = Examples.values()[position]

        holder.nameText.setText(ResourcesMapper.mapExampleName(example))
    }

}

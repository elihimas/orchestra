package com.elihimas.orchestra.ui.animateimage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.databinding.AnimationRowBinding
import com.elihimas.orchestra.listFields
import com.elihimas.orchestra.valueAsString

class AnimationsAdapter(private val animationSelected: (Animation) -> Unit) :
    RecyclerView.Adapter<AnimationsAdapter.AnimationViewHolder>() {

    inner class AnimationViewHolder(val binding: AnimationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { clickSource ->
                animationSelected(clickSource.tag as Animation)
            }
        }
    }

    var animations = mutableListOf<Animation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationViewHolder =
        AnimationViewHolder(
            AnimationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: AnimationViewHolder, position: Int) {
        val animation = animations[position]
        holder.itemView.tag = animation

        with(holder.binding) {
            nameText.text = animation.javaClass.simpleName

            paramsContainer.removeAllViews()

            val fields = animation.javaClass.listFields()
            fields.forEach { field ->
                val name = field.name
                val value = field.valueAsString(animation)

                val fieldString = "$name: $value"

                val textView = TextView(root.context).apply {
                    text = fieldString
                }

                paramsContainer.addView(textView)
            }
        }
    }

    override fun getItemCount(): Int = animations.size

}

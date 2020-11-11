package com.elihimas.orchestra.ui.animateimage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.*
import com.elihimas.orchestra.animations.Animation
import kotlinx.android.synthetic.main.animation_row.view.*

class AnimationsAdapter(private val animationSelected: (Animation) -> Unit) : RecyclerView.Adapter<AnimationsAdapter.AnimationViewHolder>() {

    class AnimationViewHolder(view: View, val nameText: TextView, val paramsContainer: ViewGroup) : RecyclerView.ViewHolder(view)

    var animations = mutableListOf<Animation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.animation_row, parent, false).let { view ->
                view.setOnClickListener { clickSource ->
                    animationSelected(clickSource.tag as Animation)
                }

                AnimationViewHolder(view, view.nameText, view.paramsContainer)
            }

    override fun onBindViewHolder(holder: AnimationViewHolder, position: Int) {
        val animation = animations[position]
        holder.itemView.tag = animation

        with(holder) {
            nameText.text = animation.javaClass.simpleName

            paramsContainer.removeAllViews()

            val fields = animation.javaClass.listFields()
            fields.forEach { field ->
                val name = field.name
                val value = field.valueAsString(animation)

                val fieldString = "$name: $value"

                val textView = TextView(itemView.context).apply {
                    text = fieldString
                }

                paramsContainer.addView(textView)
            }
        }
    }

    override fun getItemCount(): Int = animations.size

}

package com.elihimas.orchestra.ui.animateimage.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.R
import com.elihimas.orchestra.model.AnimateImageAnimations
import com.elihimas.orchestra.model.ResourcesMapper
import kotlinx.android.synthetic.main.select_animation_row.view.*

class SelectAnimationsAdapter(private val selectAnimationListener: (AnimateImageAnimations) -> Unit) :
        RecyclerView.Adapter<SelectAnimationsAdapter.AnimationHolder>() {
    class AnimationHolder(view: View, val nameText: TextView,
                          val img1: ImageView, val img2: ImageView,
                          val img3: ImageView, val img4: ImageView) : RecyclerView.ViewHolder(view)

    private val animateImageAnimations = listOf(
            AnimateImageAnimations.FadeIn, AnimateImageAnimations.FadeOut, AnimateImageAnimations.Translate,
            AnimateImageAnimations.Scale, AnimateImageAnimations.Slide, AnimateImageAnimations.SlideOut,
            AnimateImageAnimations.CircularReveal, AnimateImageAnimations.Rotate
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.select_animation_row, parent, false).let {
                AnimationHolder(it, it.tvName, it.img1, it.img2, it.img3, it.img4)
            }

    override fun onBindViewHolder(holder: AnimationHolder, position: Int) {
        val animation = animateImageAnimations[position]

        holder.nameText.setText(ResourcesMapper.map(animation))

        holder.itemView.setOnClickListener {
            selectAnimationListener.invoke(animation)
        }

        AnimationsAdapterAnimator.animate(animation, holder)
    }

    override fun getItemCount() = animateImageAnimations.size
}

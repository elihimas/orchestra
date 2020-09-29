package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.elihimas.orchestra.Animation
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.fragment_edit_animation.*

class EditAnimationFragment(private val animation: Animation,
                            private val animationEditedListener: (Animation) -> Unit)
    : DialogFragment() {

    private val adapter: FieldsAdapter by lazy { FieldsAdapter(animation) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameText.text = animation.javaClass.simpleName
        editButton.setOnClickListener {
            updateAnimation()
            animationEditedListener.invoke(animation)

            dismiss()
        }


        fieldsRecycler.adapter = adapter
    }

    private fun updateAnimation() {
        adapter.update()
    }

    companion object {
        fun newInstance(animation: Animation, animationEditedListener: (Animation) -> Unit) =
                EditAnimationFragment(animation, animationEditedListener)
    }
}

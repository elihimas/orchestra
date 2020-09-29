package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.elihimas.orchestra.R
import com.elihimas.orchestra.model.AnimateImageAnimations
import com.elihimas.orchestra.ui.animateimage.adpters.SelectAnimationsAdapter
import kotlinx.android.synthetic.main.fragment_add_animation.*

class AddAnimationFragment(private val selectAnimationListener: (AnimateImageAnimations) -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animationsRecycler.adapter = SelectAnimationsAdapter(selectAnimationListener)
    }

    companion object {
        fun newInstance(selectAnimationListener: (AnimateImageAnimations) -> Unit) =
                AddAnimationFragment(selectAnimationListener)
    }
}

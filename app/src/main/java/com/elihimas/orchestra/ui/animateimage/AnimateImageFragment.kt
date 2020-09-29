package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.fragment_animate_image.*

class ContentsAdapter(fm: FragmentManager, private vararg val fragments: Fragment) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = fragments.size
    override fun getItem(position: Int) = fragments[position]
}

class AnimateImageFragment : Fragment() {


    private val animationsFragment by lazy { AnimationsFragment() }
    private val codeFragment by lazy { CodeFragment() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_animate_image, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = (context as AppCompatActivity).supportFragmentManager

        contentsPager.adapter = ContentsAdapter(fragmentManager, animationsFragment, codeFragment)
    }

    companion object {
        fun newInstance() = AnimateImageFragment()
    }

}

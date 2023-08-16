package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elihimas.orchestra.animations.Animation
import com.elihimas.orchestra.databinding.FragmentAnimationsBinding

class AnimationsFragment : Fragment() {

    private lateinit var binding: FragmentAnimationsBinding
    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(AnimationsViewModel::class.java) }

    private val animationsAdapter by lazy {
        AnimationsAdapter { animation ->
            open(animation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.animationsRecycler.adapter = animationsAdapter

        viewModel.animationList.observe(requireActivity(), { animations ->
            animationsAdapter.animations = animations
            animationsAdapter.notifyDataSetChanged()
        })
        viewModel.loadAnimation()
    }

    private fun open(animation: Animation) {
//        val editAnimationFragment =
//            EditAnimationFragment.newInstance(animation, animationEditedListener = {
//                updateAnimation()
//            })
//        val fragmentManager = requireActivity().supportFragmentManager
//
//        editAnimationFragment.show(fragmentManager, editAnimationFragmentTag)
    }

    private fun updateAnimation() {
        animationsAdapter.notifyDataSetChanged()
    }

    private companion object {
        const val editAnimationFragmentTag = "editAnimation"
    }

}

package com.elihimas.orchestra.ui.animateimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.orchestra.animations.*

class AnimationsViewModel : ViewModel() {

    private val _animationList = mutableListOf<Animation>()
    val animationList by lazy { MutableLiveData<MutableList<Animation>>() }

    init {
        animationList.postValue(_animationList)
    }

    fun loadAnimation() {
        _animationList.addAll(mock())
        animationList.postValue(_animationList)
    }

    private fun mock() =
            mutableListOf(
                    FadeOutAnimation(),
                    FadeInAnimation(),
                    SlideAnimation(Direction.Right),
                    ScaleAnimation(0.2f),
                    ScaleAnimation(2f),
                    ScaleAnimation(1f),
                    RotateAnimation(90f),
                    CircularRevealAnimation(),
                    RotateAnimation(270f),
            )

    fun addAnimation(animation: Animation) {
        _animationList.add(animation)
        animationList.postValue(_animationList)
    }


}

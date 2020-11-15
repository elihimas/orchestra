package com.elihimas.orchestra.ui.animateimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.orchestra.animations.Animation

class AnimationsViewModel : ViewModel() {

    private val _animationList = mutableListOf<Animation>()
    val animationList by lazy { MutableLiveData<MutableList<Animation>>() }

    init {
        animationList.postValue(_animationList)
    }

    fun loadAnimation() {
        animationList.postValue(_animationList)
    }


    fun addAnimation(animation: Animation) {
        _animationList.add(animation)
        animationList.postValue(_animationList)
    }


}

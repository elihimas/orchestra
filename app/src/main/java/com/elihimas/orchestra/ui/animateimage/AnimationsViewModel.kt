package com.elihimas.orchestra.ui.animateimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elihimas.orchestra.*

class AnimationsViewModel : ViewModel() {

    val animationList by lazy { MutableLiveData<MutableList<Animation>>() }

    fun loadAnimation() {
        animationList.postValue(mock())
    }

    private fun mock() =
            mutableListOf(
                    FadeInAnimation(),
                    FadeOutAnimation(),
                    SlideAnimation(Direction.Right),
                    ScaleAnimation(3f),
                    RotateAnimation(90f),
                    CircularRevealAnimation(),
            )

    fun updateAnimations(animations: MutableList<Animation>) {
        //TODO review this method
        //animationsList.postValue(animations)
    }


}

package com.elihimas.orchestra.ui.animateimage.adpters

import com.elihimas.orchestra.animations.Direction
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.blocks.ViewReference
import com.elihimas.orchestra.model.AnimateImageAnimations

object AnimationsAdapterAnimator {

    fun animate(animations: AnimateImageAnimations, holder: SelectAnimationsAdapter.AnimationHolder) {
        Orchestra.launch {
            parallel {
                val imgReference1 = on(holder.img1)
                val imgReference2 = on(holder.img2)
                val imgReference3 = on(holder.img3)
                val imgReference4 = on(holder.img4)

                val doAnimate = when (animations) {
                    AnimateImageAnimations.FadeIn -> {
                        { fadeIn(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.FadeOut -> {
                        { fadeOut(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.Scale -> {
                        { scale(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.Translate -> {
                        {
                            translate(imgReference1, imgReference2, imgReference3, imgReference4)
                        }
                    }
                    AnimateImageAnimations.Slide -> {
                        { slide(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.SlideOut -> {
                        { slideOut(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.CircularReveal -> {
                        { circularReveal(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                    AnimateImageAnimations.Rotate -> {
                        { rotate(imgReference1, imgReference2, imgReference3, imgReference4) }
                    }
                }

                doAnimate.invoke()
            }
        }
    }

    private fun fadeIn(imgReference1: ViewReference,
                       imgReference2: ViewReference,
                       imgReference3: ViewReference,
                       imgReference4: ViewReference) {
        imgReference1.fadeIn {
            finalAlpha = 0.25f
        }
        imgReference2.fadeIn {
            finalAlpha = 0.5f
        }
        imgReference3.fadeIn {
            finalAlpha = 0.75f
        }
        imgReference4.fadeIn {
            finalAlpha = 1f
        }
    }

    private fun fadeOut(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.fadeOut {
            finalAlpha = 1f
        }
        img2.fadeOut {
            finalAlpha = 0.75f
        }
        img3.fadeOut {
            finalAlpha = 0.5f
        }
        img4.fadeOut {
            finalAlpha = 0.25f
        }
    }

    private fun scale(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.scale(0.1f)
        img2.scale(0.3f)
        img3.scale(0.6f)
        img4.scale(1)
    }

    private fun translate(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.translate(-30f, 0f)
        img2.translate(0f, -30f)
        img3.translate(30f, 0f)
        img4.translate(0f, -30f)
    }

    private fun slide(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.slide(Direction.Left)
        img2.slide(Direction.Up)
        img3.slide(Direction.Right)
        img4.slide(Direction.Down)
    }

    private fun slideOut(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.slideOut(Direction.Left)
        img2.slideOut(Direction.Up)
        img3.slideOut(Direction.Right)
        img4.slideOut(Direction.Down)
    }

    private fun circularReveal(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.circularReveal()
        img2.circularReveal()
        img3.circularReveal()
        img4.circularReveal()
    }

    private fun rotate(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.rotate(90f)
        img2.rotate(-90f)
        img3.rotate(900f)
        img4.rotate(-1030f)
    }
}

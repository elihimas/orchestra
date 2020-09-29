package com.elihimas.orchestra.ui.animateimage.adpters

import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.ViewReference
import com.elihimas.orchestra.model.AnimateImageAnimations

object AnimationsAdapterAnimator {

    fun animate(animations: AnimateImageAnimations, holder: SelectAnimationsAdapter.AnimationHolder) {
        Orchestra.launch {
            parallel {
                val img1 = on(holder.img1)
                val img2 = on(holder.img2)
                val img3 = on(holder.img3)
                val img4 = on(holder.img4)

                when (animations) {
                    AnimateImageAnimations.FadeIn -> fadeIn(img1, img2, img3, img4)
                    AnimateImageAnimations.FadeOut -> fadeOut(img1, img2, img3, img4)
                    AnimateImageAnimations.Scale -> scale(img1, img2, img3, img4)
                }
            }
        }
    }

    private fun fadeIn(img1: ViewReference, img2: ViewReference, img3: ViewReference, img4: ViewReference) {
        img1.fadeIn {
            finalAlpha = 0.25f
        }
        img2.fadeIn {
            finalAlpha = 0.5f
        }
        img3.fadeIn {
            finalAlpha = 0.75f
        }
        img4.fadeIn {
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
        img1.scale {
            scale = 0.1f
        }
        img2.scale {
            scale = 0.3f
        }
        img3.scale {
            scale = 0.6f
        }
        img4.scale {
            scale = 1f
        }
    }
}

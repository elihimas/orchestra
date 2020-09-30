package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elihimas.orchestra.FadeInAnimation
import com.elihimas.orchestra.R
import com.elihimas.orchestra.ui.animateimage.usecases.EnumToAnimationUseCase
import kotlinx.android.synthetic.main.activity_animate_image.*

class AnimateImageActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(AnimationsViewModel::class.java) }
    private val enumToAnimationUseCase by lazy { EnumToAnimationUseCase() }

    private val addAnimationFragment: AddAnimationFragment by lazy {
        AddAnimationFragment.newInstance {animationEnum->
            val animation = enumToAnimationUseCase.execute(animationEnum)
            viewModel.addAnimation(animation)
            addAnimationFragment.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate_image)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AnimateImageFragment.newInstance())
                .commitNow()

        init()
    }

    private fun init() {
        fab.setOnClickListener {
            showAddAnimationFragment()
        }
    }

    private fun showAddAnimationFragment() {
        addAnimationFragment.show(supportFragmentManager, addAnimationFragmentTag)
    }

    private companion object {
        const val addAnimationFragmentTag = "addAnimationFragmentTag"
    }

}

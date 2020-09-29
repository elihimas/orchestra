package com.elihimas.orchestra.ui.animateimage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elihimas.orchestra.R
import kotlinx.android.synthetic.main.activity_animate_image.*

class AnimateImageActivity : AppCompatActivity() {

    private val addAnimationFragment: AddAnimationFragment by lazy {
        AddAnimationFragment.newInstance {
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

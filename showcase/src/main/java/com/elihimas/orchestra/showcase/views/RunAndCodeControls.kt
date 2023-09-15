package com.elihimas.orchestra.showcase.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.elihimas.orchestra.Orchestra
import com.elihimas.orchestra.OrchestraContext
import com.elihimas.orchestra.SetupContext
import com.elihimas.orchestra.showcase.R
import com.elihimas.orchestra.showcase.databinding.RunAndCodeControlsBinding

class RunAndCodeControls(context: Context, private val attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding by lazy {
        RunAndCodeControlsBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }
    private val showButtonAlpha = .8f

    private lateinit var tvCode: TextView
    private lateinit var tvCodeContainer: View
    private var codeResId: Int = -1
    private var shouldShowRunButton = true

    init {
        initViews()
        initOrchestra()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        with(context.theme.obtainStyledAttributes(attrs, R.styleable.RunAndCodeControls, 0, 0)) {
            val tvCodeId = getResourceId(R.styleable.RunAndCodeControls_tvCodeId, R.id.tvCode)
            tvCode = (parent as ViewGroup).findViewById(tvCodeId)
            tvCodeContainer = (parent as ViewGroup).findViewById(R.id.tvCodeContainer)
            tvCode.setText(codeResId)

            tvCodeContainer.setOnClickListener{
                println()
            }
        }
    }

    private fun initViews() {
        with(binding) {
            btShowCode.setOnClickListener {
                showCode()
            }
            btHideCode.setOnClickListener {
                hideCode()
            }
        }
    }

    private fun initOrchestra() {
        Orchestra.setup {
            with(binding) {
                val viewsReference = if (shouldShowRunButton) {
                    on(btShowCode, btHideCode, btRun)
                } else {
                    on(btShowCode, btHideCode)
                }

                viewsReference.alpha(0f)
            }
        }
    }

    fun setRunAnimationListener(listener: () -> Unit) {
        binding.btRun.setOnClickListener { listener() }
    }

    fun setCodeText(@StringRes codeResId: Int) {
        this.codeResId = codeResId
    }

    fun fadeInWithContext(orchestraContext: OrchestraContext) = with(orchestraContext) {
        with(binding) {
            val viewsReference = if (shouldShowRunButton) {
                on(btShowCode, btRun)
            } else {
                on(btShowCode)
            }

            viewsReference
                .fadeIn {
                    finalAlpha = showButtonAlpha
                }
        }
    }

    private fun showCode() = with(binding) {
        Orchestra.launch {
            parallel {
                on(tvCodeContainer).parallel {
                    fadeIn {
                        duration = 300
                    }
                    scale(1f) {
                        duration = 600
                        interpolator = OvershootInterpolator()
                    }
                }

                on(btHideCode).fadeIn {
                    finalAlpha = showButtonAlpha
                }

                val viewsReference = if (shouldShowRunButton) {
                    on(btShowCode, btRun)
                } else {
                    on(btShowCode)
                }

                viewsReference.fadeOut()
            }
        }
    }

    private fun hideCode() = with(binding) {
        Orchestra.launch {
            parallel {
                on(tvCodeContainer).parallel {
                    fadeOut {
                        duration = 900
                    }
                    scale(0f) {
                        duration = 600
                        interpolator = AnticipateInterpolator()
                    }
                }


                on(btHideCode).fadeOut()

                val viewsReference = if (shouldShowRunButton) {
                    on(btShowCode, btRun)
                } else {
                    on(btShowCode)
                }

                viewsReference.fadeIn {
                    finalAlpha = showButtonAlpha
                }
            }
        }.andThen {
            tvCodeContainer.visibility = GONE
        }
    }

    fun hideWithContext(setupContext: SetupContext) = with(setupContext) {
        with(binding) {
            val viewsReference = if (shouldShowRunButton) {
                on(btShowCode, btHideCode, btRun)
            } else {
                on(btShowCode, btHideCode)
            }

            viewsReference.alpha(0f)
        }
    }

    fun hideRunAnimationButton() {
        binding.btRun.isVisible = false
        shouldShowRunButton = false
    }
}
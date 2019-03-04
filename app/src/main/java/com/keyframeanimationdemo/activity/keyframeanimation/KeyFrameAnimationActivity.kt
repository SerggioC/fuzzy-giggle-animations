package com.keyframeanimationdemo.activity.keyframeanimation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import com.keyframeanimationdemo.R
import kotlinx.android.synthetic.main.detail.*

class KeyFrameAnimationActivity : AppCompatActivity() {


    /**
     * Call this method to launch the activity.
     */
    companion object {
        fun launchActivity(context: Context) {
            val intent = Intent(context, KeyFrameAnimationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_frame_animation)

        var show = true
        iv_background_image.setOnClickListener {
            when {
                show -> goToLayout(R.layout.detail)
                else -> goToLayout(R.layout.activity_key_frame_animation)
            }
            show = !show
        }
    }

    private fun goToLayout(@LayoutRes layout: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, layout)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0.5f)
        transition.duration = 400

        TransitionManager.beginDelayedTransition(constraint, transition)

        constraintSet.applyTo(constraint)
    }

}
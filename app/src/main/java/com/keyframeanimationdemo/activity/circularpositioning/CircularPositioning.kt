package com.keyframeanimationdemo.activity.circularpositioning

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.keyframeanimationdemo.R
import kotlinx.android.synthetic.main.activity_orbits.*
import java.util.concurrent.TimeUnit


class CircularPositioning : AppCompatActivity() {

    /**
     * Call this method to launch the activity.
     */
    companion object {
        fun launchActivity(context: Context) {
            val intent = Intent(context, CircularPositioning::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orbits)

        val earthAnimator = animatePlanet(earth_image, TimeUnit.SECONDS.toMillis(2))
        val marsAnimator = animatePlanet(mars_image, TimeUnit.SECONDS.toMillis(3))
        val saturnAnimator = animatePlanet(saturn_image, TimeUnit.SECONDS.toMillis(3))

        updateConstraints(R.layout.activity_orbits)
        startAnimation(arrayOf(earthAnimator, marsAnimator, saturnAnimator))

        var show = true
        sun_image.setOnClickListener {
            if (show) {
                cancelAnim(arrayOf(earthAnimator, marsAnimator, saturnAnimator))
                sun_image.clearAnimation()
                updateConstraints(R.layout.layout_orbits_details)
            } else {
                startAnimation(arrayOf(earthAnimator, marsAnimator, saturnAnimator))
                updateConstraints(R.layout.activity_orbits)
            }
            show = !show
        }

//        val animation = loadAnimation(this, R.anim.rotate_anim)
//        sun_image.startAnimation(animation)
//        animation.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//            }
//        })

        sun_image.pivotX = 0.5f
        sun_image.pivotY = 1.0f
//        sun_image
//            .animate()
//            .setInterpolator(LinearInterpolator())
//            .setDuration(1000)
//            .rotation(6.0f)
//            .setListener(object : Animator.AnimatorListener {
//                override fun onAnimationRepeat(animation: Animator?) {
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    animation?.start()
//                    Log.i("Sergio> ", "starting animation on sun")
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                }
//
//                override fun onAnimationStart(animation: Animator?) {
//                }
//            })
//            .start()

        val deltaDegrees = 6.0f
        val startDegrees = 0.0f
        val endDegrees = startDegrees + deltaDegrees
        sun_image.animation = newRotateAnimation(startDegrees, endDegrees)

    }

    private fun newRotateAnimation(fromDegrees: Float, toDegrees: Float): RotateAnimation {
        val deltaDegrees = 6.0f
        var startDegrees = fromDegrees
        var endDegrees = toDegrees

        val rotateAnimation = RotateAnimation(fromDegrees, toDegrees)
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.duration = 100
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startDegrees += deltaDegrees
                endDegrees += deltaDegrees
                sun_image.animation = newRotateAnimation(startDegrees, endDegrees)
                Log.i("Sergio> ", "starting animation on sun: startDegrees: $startDegrees endDegrees $endDegrees")
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        return rotateAnimation
    }

    private fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(root)
        TransitionManager.beginDelayedTransition(root)
    }

    private fun cancelAnim(animator: Array<ValueAnimator>) {
        animator.forEach { it.cancel() }
    }

    private fun startAnimation(animator: Array<ValueAnimator>) {
        animator.forEach { it.start() }
    }

    private fun animatePlanet(planet: ImageView?, orbitDuration: Long): ValueAnimator {
        val animator = ValueAnimator.ofInt(0, 360)
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = planet?.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.circleAngle = value.toFloat()
            planet.layoutParams = layoutParams
            animator.duration = orbitDuration
            animator.interpolator = LinearInterpolator()
            animator.repeatMode = ValueAnimator.RESTART
            animator.repeatCount = ValueAnimator.INFINITE
        }
        return animator
    }
}

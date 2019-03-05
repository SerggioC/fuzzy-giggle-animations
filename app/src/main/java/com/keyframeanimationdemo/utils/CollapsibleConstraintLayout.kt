package com.keyframeanimationdemo.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.keyframeanimationdemo.R
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class CollapsibleConstraintLayout : ConstraintLayout, AppBarLayout.OnOffsetChangedListener {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttribute: Int) : super(
        context,
        attrs,
        defStyleAttribute
    )


    private var mTransitionThreshold = 0.35f
    private var mLastPosition: Int = 0
    private var mToolBarOpen = true

    private val mOpenToolBarSet: ConstraintSet = ConstraintSet()
    private val mCloseToolBarSet: ConstraintSet = ConstraintSet()
    private var mBackground: ImageView? = null
    private var mTitle: TextView? = null
    private var mIcon: ImageView? = null
    private var mTranslationTitle: AnimationHelper? = null
    private var mTranslationIcon: AnimationHelper? = null
    private var showImageAnimator: Animator? = null
    private var hideImageAnimator: Animator? = null
    private var iconInitialPosition: Float? = null
    private var iconMaxPosition: Float? = null
    private var icon2InitialPosition: Float? = null
    private var icon2MaxPosition: Float? = null
    private var positionsInitialized = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (parent is AppBarLayout) {
            val appBarLayout = parent as AppBarLayout
            appBarLayout.addOnOffsetChangedListener(this)
            mOpenToolBarSet.clone(context, R.layout.toolbar_layout)
            mCloseToolBarSet.clone(context, R.layout.layout_closed_toolbar)

            mBackground = findViewById(R.id.iv_product_image)
            mTitle = findViewById(R.id.tv_title)
            mIcon = findViewById(R.id.iv_icon)
            showImageAnimator = ObjectAnimator.ofFloat(mBackground, "alpha", 0f, 1f)
            showImageAnimator?.duration = 600
            hideImageAnimator = ObjectAnimator.ofFloat(mBackground, "alpha", 1f, 0f)
            hideImageAnimator?.duration = 600

        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (parent is AppBarLayout) {
            val appBarLayout = parent as AppBarLayout


        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (mLastPosition == verticalOffset) {
            return
        }

        if (positionsInitialized.not()) {
            iconInitialPosition = iv_icon.x
            iconMaxPosition = appBarLayout?.width?.toFloat()?.plus(iv_icon.width / 2)
            icon2InitialPosition = iv_icon2.x
            icon2MaxPosition = appBarLayout?.width?.toFloat()?.plus(iv_icon2.width / 2)
            positionsInitialized = true
            Log.i(
                "Sergio> ",
                "iconInitialPosition: $iconInitialPosition icon2InitialPosition $icon2InitialPosition iconMaxPosition $iconMaxPosition"
            )
        }


        mLastPosition = verticalOffset
        val progress = -verticalOffset / appBarLayout?.height?.toFloat()!!

        val iconProgress = (iconMaxPosition!! - iconInitialPosition!!) * progress
        val iconPosition = iconInitialPosition!! + iconProgress
        iv_icon?.x = iconPosition

        val icon2Progress = (icon2MaxPosition!! - iconPosition) * progress
        val icon2Position = icon2InitialPosition!! + icon2Progress
        iv_icon2?.x = icon2Position

        Log.i("Sergio> ", "progress: ${progress * 100}% \n")
        Log.i("Sergio> ", "iconProgress $iconProgress iconPosition $iconPosition")
        Log.i("Sergio> ", "icon2Progress $icon2Progress icon2Position $icon2Position")

        val params = layoutParams as AppBarLayout.LayoutParams
        params.topMargin = -verticalOffset
        layoutParams = params

//
//        if (mToolBarOpen && progress > mTransitionThreshold) {
//            mCloseToolBarSet.applyTo(this)
//            hideImageAnimator?.start()
//            mToolBarOpen = false
//        } else if (!mToolBarOpen && progress < mTransitionThreshold) {
//            mOpenToolBarSet.applyTo(this)
//            showImageAnimator?.start()
//            mToolBarOpen = true
//        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mTitle != null && mTranslationTitle == null) {
            mTranslationTitle = AnimationHelper(mTitle!!)
        }
        if (mIcon != null && mTranslationIcon == null) {
            mTranslationIcon = AnimationHelper(mIcon!!)
        }
        mTranslationTitle?.evaluate()
        mTranslationIcon?.evaluate()
    }

    class AnimationHelper(view: View) {
        private var initialValue = 0
        private var target = view

        init {
            initialValue = target.left
        }

        fun evaluate() {
            if (initialValue != target.left) {
                val delta = (initialValue - target.left).toFloat()
                val anim = ObjectAnimator.ofFloat(target, "translationX", delta, 0f)
                anim.duration = 400
                anim.start()
                initialValue = target.left
            }
        }
    }

    /** Converts dp into its equivalent px */
    private fun dpToPx(dp: Float?) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp ?: 0.0f,
            resources.displayMetrics
        )
}


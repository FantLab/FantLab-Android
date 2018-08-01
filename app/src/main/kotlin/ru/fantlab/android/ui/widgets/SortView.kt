package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper

class SortView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

	protected lateinit var mHeadLayout: FrameLayout

	protected var DEFAULT_HEIGHT = 100

	protected var mHeadHeight: Float = 0.toFloat()

	protected var isOpenMenu: Boolean = false

	private var mChildView: View? = null

	private var mTouchY: Float = 0.toFloat()

	private var mCurrentY: Float = 0.toFloat()

	private var listener: SortViewListener? = null

	private val decelerateInterpolator = DecelerateInterpolator(10f)

	init {
		init(context, attrs, defStyleAttr)
	}

	interface SortViewListener {
		fun onMenuStateChanged(isOpened: Boolean)
	}

	fun setOnSortViewListener(listener: SortViewListener) {
		this.listener = listener
	}

	private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
		if (isInEditMode) {
			return
		}

		if (childCount > 1) {
			throw RuntimeException("can only have one child widget")
		}
		val t = context.obtainStyledAttributes(attrs, R.styleable.SortView, defStyleAttr, 0)
		mHeadHeight = t.getInteger(R.styleable.SortView_menu_height, ViewHelper.toPx(getContext(), DEFAULT_HEIGHT)).toFloat()

		t.recycle()
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()

		val context = context

		val headViewLayout = FrameLayout(context)
		val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
		layoutParams.gravity = Gravity.TOP
		headViewLayout.layoutParams = layoutParams
		mHeadLayout = headViewLayout
		this.addView(mHeadLayout)

		mChildView = getChildAt(0)
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		//if (isOpenMenu) return true;
		when (ev.action) {
			MotionEvent.ACTION_DOWN -> {
				mTouchY = ev.y
				mCurrentY = mTouchY
			}
			MotionEvent.ACTION_MOVE -> {
				val currentY = ev.y
				val dy = currentY - mTouchY
				if (dy > 0 && !canChildScrollUp()) {
					return true
				}
			}
		}
		return super.onInterceptTouchEvent(ev)
	}

	override fun onTouchEvent(e: MotionEvent): Boolean {
		if (isOpenMenu) {
			return super.onTouchEvent(e)
		}
		when (e.action) {
			MotionEvent.ACTION_MOVE -> {
				mCurrentY = e.y
				var dy = mCurrentY - mTouchY
				dy = Math.min(mHeadHeight * 2, dy)
				dy = Math.max(0f, dy)
				if (mChildView != null) {
					val offsetY = decelerateInterpolator.getInterpolation(dy / mHeadHeight / 2f) * dy / 2
					val fraction = offsetY / mHeadHeight
					mHeadLayout.layoutParams.height = offsetY.toInt()
					mHeadLayout.requestLayout()
					ViewCompat.setRotationX(mHeadLayout, 90 * (1.0f - fraction))
					ViewCompat.setTranslationY(mChildView!!, offsetY)
					mHeadLayout.pivotX = (mHeadLayout.width / 2).toFloat()
					mHeadLayout.pivotY = offsetY

				}
				return true
			}
			MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
				if (mChildView != null) {
					if (ViewCompat.getTranslationY(mChildView!!) >= mHeadHeight) {
						createAnimatorTranslationY(mChildView, mHeadHeight, mHeadLayout)
						isOpenMenu = true
						listener!!.onMenuStateChanged(true)
					} else {
						createAnimatorToHeadView(mHeadLayout)
						this.postDelayed({ createAnimatorTranslationY(mChildView, 0f, mHeadLayout) }, 50)
					}
				}
				return true
			}
		}
		return super.onTouchEvent(e)
	}


	private fun createAnimatorToHeadView(v: View) {
		val viewPropertyAnimatorCompat = ViewCompat.animate(v)
		viewPropertyAnimatorCompat.duration = 200
		viewPropertyAnimatorCompat.interpolator = DecelerateInterpolator()
		viewPropertyAnimatorCompat.rotationX(90f)
		viewPropertyAnimatorCompat.start()
		viewPropertyAnimatorCompat.setUpdateListener {
			val height = ViewCompat.getTranslationY(mChildView!!)
			mHeadLayout.pivotX = (mHeadLayout.width / 2).toFloat()
			mHeadLayout.pivotY = height
		}
	}

	private fun createAnimatorTranslationY(v: View?, h: Float, fl: FrameLayout) {
		val viewPropertyAnimatorCompat = ViewCompat.animate(v)
		viewPropertyAnimatorCompat.duration = 200
		viewPropertyAnimatorCompat.interpolator = DecelerateInterpolator()
		viewPropertyAnimatorCompat.translationY(h)
		viewPropertyAnimatorCompat.start()
		viewPropertyAnimatorCompat.setUpdateListener {
			val height = ViewCompat.getTranslationY(v!!)
			fl.layoutParams.height = height.toInt()
			fl.requestLayout()
		}
	}

	fun closeMenu() {
		this.postDelayed({ closeMenuing() }, 200)
	}

	private fun closeMenuing() {
		if (mChildView != null) {
			val viewPropertyAnimatorCompat = ViewCompat.animate(mChildView)
			viewPropertyAnimatorCompat.duration = 200
			viewPropertyAnimatorCompat.y(ViewCompat.getTranslationY(mChildView!!))
			viewPropertyAnimatorCompat.translationY(0f)
			viewPropertyAnimatorCompat.interpolator = DecelerateInterpolator()
			viewPropertyAnimatorCompat.start()
		}
		isOpenMenu = false
		listener!!.onMenuStateChanged(false)
	}

	fun setHeaderView(headerView: View) {
		post { mHeadLayout.addView(headerView) }
	}

	private fun canChildScrollUp(): Boolean {
		return mChildView != null && ViewCompat.canScrollVertically(mChildView!!, -1)
	}

}

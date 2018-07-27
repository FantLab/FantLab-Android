package ru.fantlab.android.ui.widgets.ratingbar

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import ru.fantlab.android.R

class ScaleRatingBar : AnimationRatingBar {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun emptyRatingBar() {
		if (mRunnable != null) {
			mHandler?.removeCallbacksAndMessages(mRunnableToken)
		}

		val delay: Long = 0
		for (view in mPartialViews) {
			mHandler?.postDelayed({ view.setEmpty() }, delay + 5.toShort())
		}
	}

	override fun fillRatingBar(rating: Float) {
		if (mRunnable != null) {
			mHandler?.removeCallbacksAndMessages(mRunnableToken)
		}

		for (partialView in mPartialViews) {
			val ratingViewId = partialView.tag as Int
			val maxIntOfRating = Math.ceil(rating.toDouble())

			if (ratingViewId > maxIntOfRating) {
				partialView.setEmpty()
				continue
			}

			if (isInEditMode) {
				if (ratingViewId.toDouble() == maxIntOfRating) {
					partialView.setPartialFilled(rating)
				} else {
					partialView.setFilled()
				}
			} else {
				mRunnable = getAnimationRunnable(rating, partialView, ratingViewId, maxIntOfRating)
				postRunnable(mRunnable!!, ANIMATION_DELAY)
			}
		}
	}

	private fun getAnimationRunnable(rating: Float, partialView: PartialView, ratingViewId: Int, maxIntOfRating: Double): Runnable {
		return Runnable {
			if (ratingViewId.toDouble() == maxIntOfRating) {
				partialView.setPartialFilled(rating)
			} else {
				partialView.setFilled()
			}

			if (ratingViewId.toFloat() == rating) {
				val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
				val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
				partialView.startAnimation(scaleUp)
				partialView.startAnimation(scaleDown)
			}
		}
	}

	companion object {

		private val ANIMATION_DELAY: Long = 15
	}
}



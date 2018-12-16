package ru.fantlab.android.ui.widgets.ratingbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.FloatRange
import android.support.annotation.IntRange
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import ru.fantlab.android.R
import java.util.*

open class BaseRatingBar
/**
 * @param context context
 * @param attrs attributes from XML => app:mainText="mainText"
 * @param defStyleAttr attributes from default style (Application theme or activity theme)
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), SimpleRatingBar {
	lateinit var mPartialViews: MutableList<PartialView>
	private var mNumStars: Int = 0
	private var mPadding = 20
	private var mStarWidth: Int = 0
	private var mStarHeight: Int = 0
	private var mMinimumStars = 0f
	override var rating = -1f
		set(rating) {
			var rating = rating
			if (rating > mNumStars) {
				rating = mNumStars.toFloat()
			}

			if (rating < mMinimumStars) {
				rating = mMinimumStars
			}

			if (this.rating == rating) {
				return
			}

			field = rating

			if (mOnRatingChangeListener != null) {
				mOnRatingChangeListener!!.onRatingChange(this, this.rating)
			}

			fillRatingBar(rating)
		}
	override var stepSize = 1f
	private var mPreviousRating = 0f
	private var mIsIndicator = false
	override var isScrollable = true
	private var mIsClickable = true
	override var isClearRatingEnabled = true
	private var mStartX: Float = 0.toFloat()
	private var mStartY: Float = 0.toFloat()
	private var mEmptyDrawable: Drawable? = null
	private var mFilledDrawable: Drawable? = null
	private var mOnRatingChangeListener: OnRatingChangeListener? = null
	private var mOnRatingDoneListener: OnRatingDoneListener? = null

	override var numStars: Int
		get() = mNumStars
		set(numStars) {
			if (numStars <= 0) {
				return
			}

			mPartialViews.clear()
			removeAllViews()

			mNumStars = numStars
			initRatingView()
		}

	override// Unit is pixel
	var starWidth: Int
		get() = mStarWidth
		set(@IntRange(from = 0) starWidth) {
			mStarWidth = starWidth
			for (partialView in mPartialViews) {
				partialView.setStarWidth(starWidth)
			}
		}

	override// Unit is pixel
	var starHeight: Int
		get() = mStarHeight
		set(@IntRange(from = 0) starHeight) {
			mStarHeight = starHeight
			for (partialView in mPartialViews) {
				partialView.setStarHeight(starHeight)
			}
		}

	override var starPadding: Int
		get() = mPadding
		set(ratingPadding) {
			if (ratingPadding < 0) {
				return
			}

			mPadding = ratingPadding

			for (partialView in mPartialViews) {
				partialView.setPadding(mPadding, mPadding, mPadding, mPadding)
			}
		}

	init {

		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRatingBar)
		var rating = typedArray.getFloat(R.styleable.BaseRatingBar_srb_rating, 0f)

		initParamsValue(typedArray, context)
		verifyParamsValue()
		initRatingView()
		rating = rating
	}

	private fun initParamsValue(typedArray: TypedArray, context: Context) {
		mNumStars = typedArray.getInt(R.styleable.BaseRatingBar_srb_numStars, mNumStars)
		stepSize = typedArray.getFloat(R.styleable.BaseRatingBar_srb_stepSize, stepSize)
		mMinimumStars = typedArray.getFloat(R.styleable.BaseRatingBar_srb_minimumStars, mMinimumStars)
		mPadding = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starPadding, mPadding)
		mStarWidth = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starWidth, 0)
		mStarHeight = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starHeight, 0)
		mEmptyDrawable = if (typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableEmpty))
			ContextCompat.getDrawable(
					context,
					typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableEmpty, View.NO_ID)
			)
		else
			null
		mFilledDrawable = if (typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableFilled))
			ContextCompat.getDrawable(
					context,
					typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableFilled, View.NO_ID)
			)
		else
			null
		mIsIndicator = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_isIndicator, mIsIndicator)
		isScrollable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_scrollable, isScrollable)
		mIsClickable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_clickable, mIsClickable)
		isClearRatingEnabled = typedArray.getBoolean(
				R.styleable.BaseRatingBar_srb_clearRatingEnabled,
				isClearRatingEnabled
		)
		typedArray.recycle()
	}

	private fun verifyParamsValue() {
		if (mNumStars <= 0) {
			mNumStars = 5
		}

		if (mPadding < 0) {
			mPadding = 0
		}

		if (mEmptyDrawable == null) {
			mEmptyDrawable = ContextCompat.getDrawable(context, R.drawable.star_empty)
		}

		if (mFilledDrawable == null) {
			mFilledDrawable = ContextCompat.getDrawable(context, R.drawable.star_filled)
		}

		if (stepSize > 1.0f) {
			stepSize = 1.0f
		} else if (stepSize < 0.1f) {
			stepSize = 0.1f
		}

		mMinimumStars = RatingBarUtils.getValidMinimumStars(mMinimumStars, mNumStars, stepSize)
	}

	private fun initRatingView() {
		mPartialViews = ArrayList()

		for (i in 1..mNumStars) {
			val partialView = getPartialView(
					i,
					mStarWidth,
					mStarHeight,
					mPadding,
					mFilledDrawable,
					mEmptyDrawable
			)
			addView(partialView)

			mPartialViews.add(partialView)
		}
	}

	private fun getPartialView(
			partialViewId: Int, starWidth: Int, starHeight: Int, padding: Int,
			filledDrawable: Drawable?, emptyDrawable: Drawable?
	): PartialView {
		val partialView = PartialView(
				context,
				partialViewId,
				starWidth,
				starHeight,
				padding
		)
		partialView.setFilledDrawable(filledDrawable!!)
		partialView.setEmptyDrawable(emptyDrawable!!)
		return partialView
	}

	protected open fun fillRatingBar(rating: Float) {
		for (partialView in mPartialViews) {
			val ratingViewId = partialView.tag as Int
			val maxIntOfRating = Math.ceil(rating.toDouble())

			if (ratingViewId > maxIntOfRating) {
				partialView.setEmpty()
				continue
			}

			if (ratingViewId.toDouble() == maxIntOfRating) {
				partialView.setPartialFilled(rating)
			} else {
				partialView.setFilled()
			}
		}
	}

	protected open fun emptyRatingBar() {
		fillRatingBar(0f)
	}

	override fun setEmptyDrawable(drawable: Drawable) {
		mEmptyDrawable = drawable

		for (partialView in mPartialViews) {
			partialView.setEmptyDrawable(drawable)
		}
	}

	override fun setEmptyDrawableRes(@DrawableRes res: Int) {
		setEmptyDrawable(Objects.requireNonNull<Drawable>(ContextCompat.getDrawable(context, res)))
	}

	override fun setFilledDrawable(drawable: Drawable) {
		mFilledDrawable = drawable

		for (partialView in mPartialViews) {
			partialView.setFilledDrawable(drawable)
		}
	}

	override fun setFilledDrawableRes(@DrawableRes res: Int) {
		setFilledDrawable(Objects.requireNonNull<Drawable>(ContextCompat.getDrawable(context, res)))
	}

	override fun setMinimumStars(@FloatRange(from = 0.0) minimumStars: Float) {
		mMinimumStars = RatingBarUtils.getValidMinimumStars(minimumStars, mNumStars, stepSize)
	}

	override fun isClickable(): Boolean {
		return mIsClickable
	}

	override fun setClickable(clickable: Boolean) {
		this.mIsClickable = clickable
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {

		val eventX = event.x
		val eventY = event.y
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				mStartX = eventX
				mStartY = eventY
				mPreviousRating = rating
				handleClickEvent(eventX)
			}
			MotionEvent.ACTION_MOVE -> {
				if (!isScrollable) {
					return false
				}

				handleMoveEvent(eventX)
			}
			MotionEvent.ACTION_UP -> {
				if (mOnRatingDoneListener != null && isClickable) {
					mOnRatingDoneListener!!.onRatingDone(rating)
				}
				if (!RatingBarUtils.isClickEvent(mStartX, mStartY, event) || !isClickable) {
					return false
				}

				handleClickEvent(eventX)
			}
		}

		parent.requestDisallowInterceptTouchEvent(true)
		return true
	}

	override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
		return true
	}

	private fun handleMoveEvent(eventX: Float) {
		for (partialView in mPartialViews) {
			if (eventX < partialView.width / 10f + mMinimumStars * partialView.width) {
				rating = mMinimumStars
				return
			}

			if (!isPositionInRatingView(eventX, partialView)) {
				continue
			}

			var rating = RatingBarUtils.calculateRating(partialView, stepSize, eventX)

			if (this.rating != rating) {
				this.rating = rating
			}
		}
	}

	private fun handleClickEvent(eventX: Float) {
		for (partialView in mPartialViews) {
			if (!isPositionInRatingView(eventX, partialView)) {
				continue
			}

			var rating = if (stepSize == 1f)
				partialView.tag as Int
			else
				RatingBarUtils.calculateRating(
						partialView,
						stepSize,
						eventX
				)

			if (mPreviousRating == rating && isClearRatingEnabled) {
				this.rating = mMinimumStars
			} else {
				rating = rating
			}

			this.rating = (rating as Int).toFloat()

			fillRatingBar(rating.toFloat())

			break
		}
	}

	private fun isPositionInRatingView(eventX: Float, ratingView: View): Boolean {
		return eventX > ratingView.left && eventX < ratingView.right
	}

	fun setOnRatingChangeListener(onRatingChangeListener: OnRatingChangeListener) {
		mOnRatingChangeListener = onRatingChangeListener
	}

	fun setOnRatingDoneListener(onRatingDoneListener: OnRatingDoneListener) {
		mOnRatingDoneListener = onRatingDoneListener
	}

	interface OnRatingChangeListener {
		fun onRatingChange(ratingBar: BaseRatingBar, rating: Float)
	}

	interface OnRatingDoneListener {
		fun onRatingDone(mRating: Float)
	}

	companion object {

		val TAG = "RatingBar"
	}
}/* Call by xml layout */

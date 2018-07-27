package ru.fantlab.android.ui.widgets.ratingbar

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.IntRange
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

internal class PartialView : RelativeLayout {

	private var mFilledView: ImageView? = null
	private var mEmptyView: ImageView? = null
	private var mStarWidth = 0
	private var mStarHeight = 0

	constructor(context: Context, partialViewId: Int, starWidth: Int, startHeight: Int, padding: Int) : super(context) {

		mStarWidth = starWidth
		mStarHeight = startHeight

		tag = partialViewId
		setPadding(padding, padding, padding, padding)
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	private fun init() {
		val params = ViewGroup.LayoutParams(
				if (mStarWidth == 0) RelativeLayout.LayoutParams.WRAP_CONTENT else mStarWidth,
				if (mStarHeight == 0) RelativeLayout.LayoutParams.WRAP_CONTENT else mStarHeight)

		mFilledView = ImageView(context)
		mFilledView!!.scaleType = ImageView.ScaleType.CENTER_CROP
		addView(mFilledView, params)

		mEmptyView = ImageView(context)
		mEmptyView!!.scaleType = ImageView.ScaleType.CENTER_CROP
		addView(mEmptyView, params)

		setEmpty()
	}

	fun setFilledDrawable(drawable: Drawable) {
		if (drawable.constantState == null) {
			return
		}

		val clipDrawable = ClipDrawable(drawable.constantState!!.newDrawable(), Gravity.START, ClipDrawable.HORIZONTAL)
		mFilledView!!.setImageDrawable(clipDrawable)
	}

	fun setEmptyDrawable(drawable: Drawable) {
		if (drawable.constantState == null) {
			return
		}

		val clipDrawable = ClipDrawable(drawable.constantState!!.newDrawable(), Gravity.END, ClipDrawable.HORIZONTAL)
		mEmptyView!!.setImageDrawable(clipDrawable)
	}

	fun setFilled() {
		mFilledView!!.setImageLevel(10000)
		mEmptyView!!.setImageLevel(0)
	}

	fun setPartialFilled(rating: Float) {
		val percentage = rating % 1
		var level = (10000 * percentage).toInt()
		level = if (level == 0) 10000 else level
		mFilledView!!.setImageLevel(level)
		mEmptyView!!.setImageLevel(10000 - level)
	}

	fun setEmpty() {
		mFilledView!!.setImageLevel(0)
		mEmptyView!!.setImageLevel(10000)
	}

	fun setStarWidth(@IntRange(from = 0) starWidth: Int) {
		mStarWidth = starWidth

		val params = mFilledView!!.layoutParams
		params.width = mStarWidth
		mFilledView!!.layoutParams = params
		mEmptyView!!.layoutParams = params
	}

	fun setStarHeight(@IntRange(from = 0) starHeight: Int) {
		mStarHeight = starHeight

		val params = mFilledView!!.layoutParams
		params.height = mStarHeight
		mFilledView!!.layoutParams = params
		mEmptyView!!.layoutParams = params
	}
}

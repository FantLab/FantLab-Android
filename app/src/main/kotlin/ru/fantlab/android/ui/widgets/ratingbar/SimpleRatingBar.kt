package ru.fantlab.android.ui.widgets.ratingbar

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.FloatRange

internal interface SimpleRatingBar {

	var numStars: Int

	var rating: Float

	var starWidth: Int

	var starHeight: Int

	var starPadding: Int

	var isScrollable: Boolean

	//var isClickable: Boolean

	var isClearRatingEnabled: Boolean

	var stepSize: Float

	fun setEmptyDrawable(drawable: Drawable)

	fun setEmptyDrawableRes(@DrawableRes res: Int)

	fun setFilledDrawable(drawable: Drawable)

	fun setFilledDrawableRes(@DrawableRes res: Int)

	fun setMinimumStars(@FloatRange(from = 0.0) minimumStars: Float)


}

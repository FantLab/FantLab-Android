package ru.fantlab.android.ui.widgets.ratingbar

import android.view.MotionEvent

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

internal object RatingBarUtils {

	private var mDecimalFormat: DecimalFormat? = null
	private val MAX_CLICK_DISTANCE = 5
	private val MAX_CLICK_DURATION = 200

	val decimalFormat: DecimalFormat
		get() {
			if (mDecimalFormat == null) {
				val symbols = DecimalFormatSymbols(Locale.ENGLISH)
				symbols.decimalSeparator = '.'
				mDecimalFormat = DecimalFormat("#.##", symbols)
			}
			return mDecimalFormat as DecimalFormat
		}

	fun isClickEvent(startX: Float, startY: Float, event: MotionEvent): Boolean {
		val duration = (event.eventTime - event.downTime).toFloat()
		if (duration > MAX_CLICK_DURATION) {
			return false
		}

		val differenceX = Math.abs(startX - event.x)
		val differenceY = Math.abs(startY - event.y)
		return !(differenceX > MAX_CLICK_DISTANCE || differenceY > MAX_CLICK_DISTANCE)
	}

	fun calculateRating(partialView: PartialView, stepSize: Float, eventX: Float): Float {
		val decimalFormat = RatingBarUtils.decimalFormat
		val ratioOfView = java.lang.Float.parseFloat(decimalFormat.format(((eventX - partialView.left) / partialView.width).toDouble()))
		val steps = Math.round(ratioOfView / stepSize) * stepSize
		return java.lang.Float.parseFloat(decimalFormat.format((partialView.tag as Int - (1 - steps)).toDouble()))
	}

	fun getValidMinimumStars(minimumStars: Float, numStars: Int, stepSize: Float): Float {
		var minimumStars = minimumStars
		if (minimumStars < 0) {
			minimumStars = 0f
		}

		if (minimumStars > numStars) {
			minimumStars = numStars.toFloat()
		}

		if (minimumStars % stepSize != 0f) {
			minimumStars = stepSize
		}
		return minimumStars
	}
}

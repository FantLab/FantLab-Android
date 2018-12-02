package ru.fantlab.android.ui.widgets.chartbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

class ChartBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
		View(context, attrs) {

	private lateinit var points: ArrayList<Pair<String, Int>>
	private var colored: Boolean = false
	private var rect = Rect()

	private var paint: Paint = Paint().apply {
		style = Paint.Style.FILL
		textSize = mainTextSize * resources.displayMetrics.scaledDensity
		isAntiAlias = true
	}

	fun setPoints(points: ArrayList<Pair<String, Int>>, colored: Boolean) {
		this.points = points
		this.colored = colored
		layoutParams.height = points.size * barHeight
		requestLayout()
	}

	override fun onDraw(canvas: Canvas) {
		val maxText = points.maxBy { it.first.length }?.first.toString()
		paint.getTextBounds(maxText, 0, maxText.length, rect)
		val leftPadding = rect.width() + startMargin

		val maxValue = points.maxBy { it.second }?.second ?: 1
		val barHeight = (height / points.size).toFloat()
		val startX = leftPadding + barStartMargin

		points.forEachIndexed { index, pair ->
			val percent = (pair.second / maxValue.toFloat())

			paint.textSize = smallTextSize * resources.displayMetrics.scaledDensity
			paint.getTextBounds(pair.second.toString(), 0, pair.second.toString().length, rect)

			val barWidth = if (pair.second == 0) 1f else (percent * (width - (leftPadding + rect.width() + barStartMargin + barEndMargin + startMargin)))

			val startY = index * barHeight + divider
			val stopX = startX + barWidth
			val stopY = startY + barHeight - (divider * 1.5f)

			paint.textSize = mainTextSize * resources.displayMetrics.scaledDensity
			paint.style = Paint.Style.STROKE
			paint.strokeWidth = 3f
			paint.color = Color.BLACK
			canvas.drawRect(startX, startY, stopX, stopY, paint)

			paint.style = Paint.Style.FILL
			paint.color = if (colored) getColor(index / points.size.toFloat()) else barColor
			canvas.drawRect(startX, startY, stopX, stopY, paint)
			paint.getTextBounds(pair.first, 0, pair.first.length, rect)

			val labelX = leftPadding - rect.width() - labelMargin
			val labelY = (((stopY - startY) / 2f) + startY) + (rect.height() / 2f)
			paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
			paint.color = labelTextColor
			canvas.drawText(pair.first, labelX, labelY, paint)

			paint.getTextBounds(pair.second.toString(), 0, pair.second.toString().length, rect)
			val valueX = stopX + barStartMargin
			val valueY = (((stopY - startY) / 2f) + startY) + (rect.height() / 2f)
			paint.textSize = smallTextSize * resources.displayMetrics.scaledDensity
			paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
			paint.color = valueTextColor
			canvas.drawText(pair.second.toString(), valueX, valueY, paint)
		}
	}

	private fun getColor(percent: Float): Int {
		val hsva = FloatArray(3)
		val hsvb = FloatArray(3)
		Color.colorToHSV(colorStart, hsva)
		Color.colorToHSV(colorEnd, hsvb)
		for (i in 0..2) {
			hsvb[i] = hsva[i] + (hsvb[i] - hsva[i]) * percent
		}
		return Color.HSVToColor(hsvb)
	}

	companion object {
		const val barHeight = 55
		const val divider = 8
		const val mainTextSize = 12f
		const val smallTextSize = 10f
		const val barStartMargin = 15
		const val barEndMargin = 15
		const val labelMargin = 5f
		const val startMargin = 10f

		@JvmField
		val colorStart = Color.parseColor("#19FF00")
		@JvmField
		val colorEnd = Color.parseColor("#FF1900")
		@JvmField
		val barColor = Color.parseColor("#3178A8")
		@JvmField
		val valueTextColor = Color.parseColor("#6d6d6d")
		@JvmField
		val labelTextColor = Color.parseColor("#4A4A4A")
	}

}
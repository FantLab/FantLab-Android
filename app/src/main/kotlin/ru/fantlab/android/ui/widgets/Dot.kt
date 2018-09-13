package ru.fantlab.android.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import ru.fantlab.android.R

class Dot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
		View(context, attrs) {

	private lateinit var paint: Paint
	var color: Color = Color.GREY
		set(value) {
			field = value
			paint = Paint().apply {
				color = ContextCompat.getColor(context, field.value)
				isAntiAlias = true
			}
			invalidate()
		}

	override fun onDraw(canvas: Canvas) {
		if (this::paint.isInitialized) {
			canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)
		}
	}

	enum class Color(@ColorRes val value: Int) {
		GREY(R.color.material_grey_500),
		RED(R.color.material_red_500),
		ORANGE(R.color.material_orange_500),
		GREEN(R.color.material_green_500)
	}
}
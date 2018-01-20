package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.TooltipCompat
import android.util.AttributeSet
import ru.fantlab.android.helper.ViewHelper

class ForegroundImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatImageView(context, attrs, defStyleAttr) {

	@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
		if (contentDescription != null) {
			TooltipCompat.setTooltipText(this, contentDescription)
		}
	}

	fun tintDrawableColor(@ColorInt color: Int) {
		ViewHelper.tintDrawable(drawable, color)
	}
}
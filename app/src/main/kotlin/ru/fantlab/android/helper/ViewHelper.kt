package ru.fantlab.android.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.design.widget.TabLayout
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import ru.fantlab.android.R

object ViewHelper {

	@ColorInt
	fun getPrimaryColor(context: Context): Int {
		return getColorAttr(context, R.attr.colorPrimary)
	}

	@ColorInt
	fun getPrimaryDarkColor(context: Context): Int {
		return getColorAttr(context, R.attr.colorPrimaryDark)
	}

	@ColorInt
	fun getListDivider(context: Context): Int {
		return getColorAttr(context, R.attr.dividerColor)
	}

	@ColorInt
	private fun getColorAttr(context: Context, attr: Int): Int {
		val theme = context.theme
		val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
		val color = typedArray.getColor(0, Color.LTGRAY)
		typedArray.recycle()
		return color
	}

	@ColorInt
	fun getTertiaryTextColor(context: Context): Int {
		return getColorAttr(context, android.R.attr.textColorTertiary)
	}

	fun tintDrawable(drawable: Drawable, @ColorInt color: Int) {
		drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
	}

	fun toPx(context: Context, dp: Int): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp.toFloat(), context.resources.displayMetrics).toInt()
	}

	private fun isTablet(resources: Resources): Boolean {
		return resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
	}

	fun isTablet(context: Context?): Boolean {
		return context != null && isTablet(context.resources)
	}

	fun getTabTextView(tabs: TabLayout, tabIndex: Int): TextView {
		return ((tabs.getChildAt(0) as LinearLayout).getChildAt(tabIndex) as LinearLayout).getChildAt(1) as TextView
	}
}
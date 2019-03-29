package ru.fantlab.android.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ScaleDrawable
import android.os.Parcelable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import com.crashlytics.android.Crashlytics
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import ru.fantlab.android.R
import ru.fantlab.android.helper.TypeFaceHelper
import ru.fantlab.android.helper.ViewHelper


/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
open class FontTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
		AppCompatTextView(context, attrs, defStyleAttr) {

	@State
	var tintColor = -1

	@State
	var selectedState: Boolean = false

	init {
		init(context, attrs)
	}

	override fun onSaveInstanceState(): Parcelable? {
		return StateSaver.saveInstanceState(this, super.onSaveInstanceState())
	}

	override fun onRestoreInstanceState(state: Parcelable) {
		super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, state))
		tintDrawables(tintColor)
		isSelected = selectedState
	}

	override fun setSelected(selected: Boolean) {
		super.setSelected(selected)
		this.selectedState = selected
	}

	@SuppressLint("SetTextI18n")
	override fun setText(text: CharSequence, type: TextView.BufferType) {
		try {
			super.setText(text, type)
		} catch (e: Exception) {
			setText("I tried, but your OEM just sucks because they modify the framework components and therefore causing the app to crash!\nFantLab")
			Crashlytics.logException(e)
		}
	}

	private fun init(context: Context, attributeSet: AttributeSet?) {
		if (attributeSet != null) {
			val tp = context.obtainStyledAttributes(attributeSet, R.styleable.FontTextView)
			try {
				val color = tp.getColor(R.styleable.FontTextView_drawableColor, -1)
				tintDrawables(color)
			} finally {
				tp.recycle()
			}
		}
		if (isInEditMode) return
		freezesText = true
		TypeFaceHelper.applyTypeface(this)
	}

	fun tintDrawables(@ColorInt color: Int) {
		if (color != -1) {
			this.tintColor = color
			val drawables = compoundDrawablesRelative
			drawables
					.filterNotNull()
					.forEach { ViewHelper.tintDrawable(it, color) }
		}
	}

	fun setEventsIcon(@DrawableRes drawableRes: Int) {
		val drawable = ContextCompat.getDrawable(context, drawableRes)
		val width = drawable!!.intrinsicWidth
		val height = drawable.intrinsicHeight
		drawable.setBounds(0, 0, width / 2, height / 2)
		val sd = ScaleDrawable(drawable, Gravity.CENTER, 0.6f, 0.6f)
		sd.level = 8000
		ViewHelper.tintDrawable(drawable, ViewHelper.getTertiaryTextColor(context))
		setCompoundDrawablesWithIntrinsicBounds(sd, null, null, null)
	}

	fun setDrawables(@DrawableRes left: Int = 0, @DrawableRes top: Int = 0, @DrawableRes right: Int = 0, @DrawableRes bottom: Int = 0) {
		setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
		val drawables = compoundDrawables
		if (drawables.isNotEmpty()) drawables[0].colorFilter = PorterDuffColorFilter(this.tintColor, PorterDuff.Mode.SRC_IN)
	}
}

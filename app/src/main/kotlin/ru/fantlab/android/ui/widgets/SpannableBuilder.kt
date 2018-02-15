package ru.fantlab.android.ui.widgets

import android.graphics.Typeface.BOLD
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import ru.fantlab.android.helper.InputHelper

/**
 * Created by Kosh on 15 Nov 2016, 9:26 PM
 */

class SpannableBuilder private constructor() : SpannableStringBuilder() {

	fun append(text: CharSequence, span: Any?): SpannableBuilder {
		if (!InputHelper.isEmpty(text)) {
			append(text = text)
			span?.let {
				val length = length
				setSpan(it, length - text.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			}
		}
		return this
	}

	override fun append(text: Char): SpannableBuilder {
		if (text.toInt() != 0) super.append(text)
		return this
	}

	override fun append(text: CharSequence?): SpannableBuilder {
		text?.let {
			super.append(text)
		}
		return this
	}

	fun append(span: Any): SpannableBuilder {
		setSpan(span, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
		return this
	}

	fun append(drawable: Drawable?): SpannableBuilder {
		drawable?.let {
			it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
			append(' ', ImageSpan(it))
		}
		return this
	}

	fun append(text: Char, span: Any): SpannableBuilder {
		append(text)
		if (!InputHelper.isEmpty(span)) {
			val length = length
			setSpan(span, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
		}
		return this
	}

	fun bold(_text: CharSequence, span: Any): SpannableBuilder {
		var text = _text
		if (!InputHelper.isEmpty(text)) {
			text = SpannableBuilder.builder().bold(text)
			append(text, span)
		}
		return this
	}

	fun bold(text: CharSequence): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, StyleSpan(BOLD)) else this
	}

	fun background(text: CharSequence, color: Int): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, BackgroundColorSpan(color)) else this
	}

	fun foreground(text: CharSequence, @ColorInt color: Int): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, ForegroundColorSpan(color)) else this
	}

	fun foreground(text: Char, @ColorInt color: Int): SpannableBuilder {
		return append(text, ForegroundColorSpan(color))
	}

	fun url(text: CharSequence, listener: View.OnClickListener): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, object : URLSpan(text.toString()) {
			override fun onClick(widget: View) {
				listener.onClick(widget)
			}
		}) else this
	}

	fun url(text: CharSequence): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, URLSpan(text.toString())) else this
	}

	fun clickable(text: CharSequence, listener: View.OnClickListener): SpannableBuilder {
		return if (!InputHelper.isEmpty(text)) append(text, object : ClickableSpan() {
			override fun updateDrawState(ds: TextPaint) {
				ds.color = ds.linkColor
				ds.isUnderlineText = false
			}

			override fun onClick(widget: View) {
				listener.onClick(widget)
			}
		}) else this
	}

	companion object {

		fun builder(): SpannableBuilder {
			return SpannableBuilder()
		}
	}
}

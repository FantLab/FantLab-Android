package ru.fantlab.android.ui.widgets.markdown.style

import android.text.TextPaint
import android.text.style.URLSpan

class LinkSpan(url: String, private val color: Int) : URLSpan(url) {

	override fun updateDrawState(ds: TextPaint) {
		super.updateDrawState(ds)
		ds.color = color
		ds.isUnderlineText = false
	}

}

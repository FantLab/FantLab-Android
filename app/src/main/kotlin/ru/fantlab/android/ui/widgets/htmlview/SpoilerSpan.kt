package ru.fantlab.android.ui.widgets.htmlview

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import ru.fantlab.android.App
import ru.fantlab.android.R

class SpoilerSpan : ClickableSpan() {

	var shown = false
	var informed = false

	override fun onClick(widget: View) {
		val context = App.instance
		shown = !shown
		if (shown && !informed) {
			Toasty.warning(context, context.getString(R.string.spoiler), Toast.LENGTH_LONG).show()
			informed = true
		}
		widget.invalidate()
	}

	override fun updateDrawState(ds: TextPaint) {
		if (shown) {
			ds.color = Color.GRAY
			ds.bgColor = Color.TRANSPARENT
		} else {
			ds.color = Color.TRANSPARENT
			ds.bgColor = Color.parseColor("#cccccc")
		}
	}
}
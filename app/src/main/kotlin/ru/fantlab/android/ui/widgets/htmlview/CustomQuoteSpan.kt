package ru.fantlab.android.ui.widgets.htmlview

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.QuoteSpan

class CustomQuoteSpan(color: Int) : QuoteSpan(color) {

	override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int,
								   first: Boolean, layout: Layout) {
		val style = p.style
		val color = p.color
		p.style = Paint.Style.FILL
		p.color = getColor()
		c.drawRect(x.toFloat(), top.toFloat(), (x + dir * STRIPE_WIDTH).toFloat(), bottom.toFloat(), p)
		p.style = style
		p.color = color
	}

	override fun getLeadingMargin(first: Boolean): Int {
		return STRIPE_WIDTH + GAP_WIDTH
	}

	companion object {
		private const val STRIPE_WIDTH = 15
		private const val GAP_WIDTH = 40
	}

}
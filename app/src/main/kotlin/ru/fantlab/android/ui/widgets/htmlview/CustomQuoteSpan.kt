package ru.fantlab.android.ui.widgets.htmlview

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan

class CustomQuoteSpan(val backgroundColor: Int, val lineColor: Int) : LeadingMarginSpan, LineBackgroundSpan {

	override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int,
								   first: Boolean, layout: Layout) {
		val style = p.style
		val color = p.color
		p.style = Paint.Style.FILL
		p.color = lineColor
		c.drawRect(x.toFloat(), top.toFloat(), x.toFloat() + dir * STRIPE_WIDTH, bottom.toFloat(), p)
		p.style = style
		p.color = color
	}

	override fun drawBackground(c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int) {
		val paintColor = p.color
		p.color = backgroundColor
		c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), p)
		p.color = paintColor
	}

	override fun getLeadingMargin(first: Boolean): Int {
		return STRIPE_WIDTH + GAP_WIDTH
	}

	companion object {
		private const val STRIPE_WIDTH = 15
		private const val GAP_WIDTH = 40
	}

}
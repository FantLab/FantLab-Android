package ru.fantlab.android.ui.widgets

import android.graphics.RectF
import android.text.Selection
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.ui.modules.edition.EditionPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity

class CustomLinkMovementMethod : LinkMovementMethod() {
	private val touchedLineBounds = RectF()
	private var isUrlHighlighted: Boolean = false
	private var clickableSpanUnderTouchOnActionDown: ClickableSpan? = null
	private var activeTextViewHashcode: Int = 0

	fun newInstance(): LinkMovementMethod {
		return LinkMovementMethod()
	}

	override fun onTouchEvent(textView: TextView, text: Spannable, event: MotionEvent): Boolean {
		if (activeTextViewHashcode != textView.hashCode()) {
			activeTextViewHashcode = textView.hashCode()
			textView.autoLinkMask = 0
		}

		val clickableSpanUnderTouch = findClickableSpanUnderTouch(textView, text, event)
		if (event.action == MotionEvent.ACTION_DOWN) {
			clickableSpanUnderTouchOnActionDown = clickableSpanUnderTouch
		}
		val touchStartedOverAClickableSpan = clickableSpanUnderTouchOnActionDown != null

		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				if (clickableSpanUnderTouch != null) {
					highlightUrl(textView, clickableSpanUnderTouch, text)
				}

				return touchStartedOverAClickableSpan
			}

			MotionEvent.ACTION_UP -> {
				if (touchStartedOverAClickableSpan && clickableSpanUnderTouch === clickableSpanUnderTouchOnActionDown) {
					dispatchUrlClick(textView, clickableSpanUnderTouch!!)
				}
				cleanupOnTouchUp(textView)

				return touchStartedOverAClickableSpan
			}

			MotionEvent.ACTION_CANCEL -> {
				cleanupOnTouchUp(textView)
				return false
			}

			MotionEvent.ACTION_MOVE -> {

					if (clickableSpanUnderTouch != null) {
						highlightUrl(textView, clickableSpanUnderTouch, text)
					} else {
						removeUrlHighlightColor(textView)
					}


				return touchStartedOverAClickableSpan
			}

			else -> return false
		}
	}

	private fun cleanupOnTouchUp(textView: TextView) {
		clickableSpanUnderTouchOnActionDown = null
		removeUrlHighlightColor(textView)
	}

	private fun findClickableSpanUnderTouch(textView: TextView, text: Spannable, event: MotionEvent): ClickableSpan? {
		var touchX = event.x.toInt()
		var touchY = event.y.toInt()

		touchX -= textView.totalPaddingLeft
		touchY -= textView.totalPaddingTop

		touchX += textView.scrollX
		touchY += textView.scrollY

		val layout = textView.layout
		val touchedLine = layout.getLineForVertical(touchY)
		val touchOffset = layout.getOffsetForHorizontal(touchedLine, touchX.toFloat())

		touchedLineBounds.left = layout.getLineLeft(touchedLine)
		touchedLineBounds.top = layout.getLineTop(touchedLine).toFloat()
		touchedLineBounds.right = layout.getLineWidth(touchedLine) + touchedLineBounds.left
		touchedLineBounds.bottom = layout.getLineBottom(touchedLine).toFloat()

		if (touchedLineBounds.contains(touchX.toFloat(), touchY.toFloat())) {
			val spans = text.getSpans(touchOffset, touchOffset, ClickableSpan::class.java)
			for (span in spans) {
				if (span is ClickableSpan) {
					return span
				}
			}
			return null

		} else {
			return null
		}
	}

	private fun highlightUrl(textView: TextView, clickableSpan: ClickableSpan, text: Spannable) {
		if (isUrlHighlighted) {
			return
		}
		isUrlHighlighted = true

		val spanStart = text.getSpanStart(clickableSpan)
		val spanEnd = text.getSpanEnd(clickableSpan)
		val highlightSpan = BackgroundColorSpan(textView.highlightColor)
		text.setSpan(highlightSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

		textView.setTag(R.id.highlight_background_span, highlightSpan)

		Selection.setSelection(text, spanStart, spanEnd)
	}

	private fun removeUrlHighlightColor(textView: TextView) {
		if (!isUrlHighlighted) {
			return
		}
		isUrlHighlighted = false

		val text = textView.text as Spannable
		val highlightSpan = textView.getTag(R.id.highlight_background_span) as BackgroundColorSpan
		text.removeSpan(highlightSpan)

		Selection.removeSelection(text)
	}

	private fun dispatchUrlClick(textView: TextView, clickableSpan: ClickableSpan) {
		val clickableSpanWithText = ClickableSpanWithText.ofSpan(textView, clickableSpan)
		val pattern = Regex("/([a-z]+).*?(\\d+)")
		val results = pattern.matchEntire(clickableSpanWithText.text())?.groupValues
		if (results != null) {
			val type = results[1]
			val id = results[2]
			val label = ClickableSpanWithText.label(textView, clickableSpan)
			when (type) {
				"work" -> {
					WorkPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				"edition" -> {
					EditionPagerActivity.startActivity(App.instance.applicationContext, id.toInt(), label, 0)
				}
				else -> {
					clickableSpanWithText.span().onClick(textView)
				}
			}
		}
	}

	private class ClickableSpanWithText private constructor(private val span: ClickableSpan, private val text: String) {

		fun span(): ClickableSpan {
			return span
		}

		fun text(): String {
			return text
		}

		companion object {

			fun ofSpan(textView: TextView, span: ClickableSpan): ClickableSpanWithText {
				val s = textView.text as Spanned
				val text: String
				text = if (span is URLSpan) {
					span.url
				} else {
					val start = s.getSpanStart(span)
					val end = s.getSpanEnd(span)
					s.subSequence(start, end).toString()
				}
				return ClickableSpanWithText(span, text)
			}

			fun label(textView: TextView, span: ClickableSpan): String {
				val s = textView.text as Spanned
				val start = s.getSpanStart(span)
				val end = s.getSpanEnd(span)
				return s.subSequence(start, end).toString().replace("\"", "")
			}
		}
	}

}
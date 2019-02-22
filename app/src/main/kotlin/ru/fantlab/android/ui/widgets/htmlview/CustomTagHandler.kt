package ru.fantlab.android.ui.widgets.htmlview

import android.graphics.Color
import android.text.*
import android.text.Html.TagHandler
import android.text.style.AlignmentSpan
import org.xml.sax.XMLReader
import ru.fantlab.android.helper.PrefGetter

class CustomTagHandler : TagHandler {

	override fun handleTag(opening: Boolean, tag: String, output: Editable,
						   xmlReader: XMLReader) {
		when (tag) {
			"h", "spoiler" -> {
				if (opening) startSpoiler(output, SpoilerSpan())
				else end(output, SpoilerSpan::class.java, SpoilerSpan())
			}
			"q" ->
				if (opening) startBlockquote(output)
				else endBlockquote(output)
		}
	}

	private fun startSpoiler(text: Editable, mark: Any) {
		val len = text.length
		text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK)
	}

	private fun startBlockquote(text: Editable) {
		startBlockElement(text, getMarginBlockquote())
		start(text, Blockquote())
	}

	private fun endBlockquote(text: Editable) {
		endBlockElement(text)
		val theme = PrefGetter.getThemeType().ordinal
		end(text, Blockquote::class.java, CustomQuoteSpan(getWindowBackground(theme)))
	}

	private fun endBlockElement(text: Editable) {
		val n = getLast(text, Newline::class.java)
		if (n != null) {
			appendNewlines(text, n.mNumNewlines)
			text.removeSpan(n)
		}

		val a = getLast(text, Alignment::class.java)
		if (a != null) {
			setSpanFromMark(text, a, AlignmentSpan.Standard(a.mAlignment))
		}
	}

	private fun getMarginBlockquote(): Int {
		return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
	}

	private fun getMargin(flag: Int): Int {
		return if (flag != 0) {
			1
		} else 2
	}

	private class Blockquote

	private fun startBlockElement(text: Editable, margin: Int) {
		val len = text.length
		if (margin > 0) {
			appendNewlines(text, margin)
			start(text, Newline(margin))
		}
	}

	private fun setSpanFromMark(text: Spannable, mark: Any, vararg spans: Any) {
		val where = text.getSpanStart(mark)
		text.removeSpan(mark)
		val len = text.length
		if (where != len) {
			for (span in spans) {
				text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			}
		}
	}

	private class Alignment(val mAlignment: Layout.Alignment)

	private class Newline(val mNumNewlines: Int)

	private fun start(text: Editable, mark: Any) {
		val len = text.length
		text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
	}

	private fun end(text: Editable, kind: Class<*>, repl: Any) {
		val len = text.length
		val obj = getLast(text, kind)
		if (obj != null) {
			setSpanFromMark(text, obj, repl)
		}
	}

	private fun appendNewlines(text: Editable, minNewline: Int) {
		val len = text.length

		if (len == 0) {
			return
		}

		var existingNewlines = 0
		var i = len - 1
		while (i >= 0 && text[i] == '\n') {
			existingNewlines++
			i--
		}

		for (j in existingNewlines until minNewline) {
			text.append("\n")
		}
	}

	private fun <T> getLast(text: Spanned, kind: Class<T>): T? {
		val objs = text.getSpans(0, text.length, kind)

		return if (objs.size == 0) {
			null
		} else {
			objs[objs.size - 1]
		}
	}

	private fun getWindowBackground(theme: Int): Int {
		return if (theme == PrefGetter.DARK) {
			Color.parseColor("#22252A")
		} else {
			Color.parseColor("#EEEEEE")
		}
	}
}
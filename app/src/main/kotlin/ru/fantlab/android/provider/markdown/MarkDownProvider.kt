package ru.fantlab.android.provider.markdown

import android.text.Html
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.TextView
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.markdown.extension.innertags.InnerTagExtension
import ru.fantlab.android.provider.markdown.extension.smiles.SmileExtension
import ru.fantlab.android.provider.timeline.HtmlHelper
import java.util.*



object MarkDownProvider {

	fun setMdText(textView: TextView, markdown: String) {
		if (!markdown.isEmpty()) {
			val width = textView.measuredWidth
			if (width > 0) {
				render(textView, markdown, width)
			} else {
				textView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
					override fun onPreDraw(): Boolean {
						textView.viewTreeObserver.removeOnPreDrawListener(this)
						render(textView, markdown, textView.measuredWidth)
						return true
					}
				})
			}
		}
	}

	fun setMdText(textView: TextView, markdown: String, width: Int) {
        if (!InputHelper.isEmpty(markdown)) {
            render(textView, markdown, width)
		}
    }

	private fun render(textView: TextView, markdown: String, width: Int) {
		val extensions = Arrays.asList(
				//StrikethroughExtension.create(),
				InnerTagExtension.create(),
				AutolinkExtension.create(),
				SmileExtension.create()
		)

		val parser = Parser.builder()
				.extensions(extensions)
				.enabledBlockTypes(HashSet(Arrays.asList()))
				.build()

		try {
			val node = parser.parse(markdown)
			val rendered = HtmlRenderer
					.builder()
					.extensions(extensions)
					.build()
					.render(node)
			HtmlHelper.htmlIntoTextView(textView, rendered, width - (textView.paddingStart + textView.paddingEnd))
		} catch (ignored: Exception) {
			HtmlHelper.htmlIntoTextView(textView, markdown, width - (textView.paddingStart + textView.paddingEnd))
		}

	}

	fun stripMdText(textView: TextView, markdown: String) {
		if (!InputHelper.isEmpty(markdown)) {
			val parser: Parser = Parser.builder().build()
			val node: Node = parser.parse(markdown)
			textView.text = stripHtml(HtmlRenderer.builder().build().render(node))
		}
	}

	fun stripMdText(markdown: String): String {
		if (!markdown.isEmpty()) {
			val parser = Parser.builder().build()
			val node = parser.parse(markdown)
			return stripHtml(HtmlRenderer.builder().build().render(node))
		}
		return ""
	}

	fun stripHtml(html: String): String {
		return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
		} else {
			Html.fromHtml(html).toString()
		}
	}

	fun addList(editText: EditText, list: String) {
		val tag = "$list "
		val source = editText.text.toString()
		var selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		var substring = source.substring(0, selectionStart)
		val line = substring.lastIndexOf(10.toChar())
		selectionStart = if (line != -1) {
			line + 1
		} else {
			0
		}
		substring = source.substring(selectionStart, selectionEnd)
		val split = substring.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val stringBuffer = StringBuilder()
		if (!source.startsWith("<LIST>")){
			stringBuffer.append("\n<LIST>")
		}
		if (split.isNotEmpty())
			for (s in split) {
				if (s.isEmpty() && stringBuffer.isNotEmpty()) {
					stringBuffer.append("\n")
					continue
				}
				if (!s.trim { it <= ' ' }.startsWith(tag)) {
					if (stringBuffer.isNotEmpty()) stringBuffer.append("\n")
					stringBuffer.append(tag).append(s)
				} else {
					if (stringBuffer.isNotEmpty()) stringBuffer.append("\n")
					stringBuffer.append(s)
				}
			}

		if (stringBuffer.isEmpty()) {
			stringBuffer.append(tag)
		}
		if (!source.startsWith("<LIST>")){
			stringBuffer.append("\n").append("</LIST>")
		}

		editText.text.replace(selectionStart, selectionEnd, stringBuffer.toString())
		editText.setSelection(stringBuffer.length + selectionStart)
	}

	fun addItalic(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "<i>$substring</i>"
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	fun addBold(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "<b>$substring</b>"
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	fun addSpoiler(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "\n<h>$substring</h>"
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	fun addStrikeThrough(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "<s>$substring</s>"
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	fun addQuote(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result: String
		if (hasNewLine(source, selectionStart)) {
			result = "<q>$substring</q>"
		} else {
			result = "\n<q>$substring</q>"

		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart)
	}

	fun addPhoto(editText: EditText, title: String, link: String) {
		val result = "[img]$link[img]"
		insertAtCursor(editText, result)
	}

	fun addLink(editText: EditText, title: String, link: String) {
		val result = "[url=$link]$title[/url]"
		insertAtCursor(editText, result)
	}

	private fun hasNewLine(source: String, selectionStart: Int): Boolean {
		var source = source
		try {
			if (source.isEmpty()) return true
			source = source.substring(0, selectionStart)
			return source[source.length - 1].toInt() == 10
		} catch (e: StringIndexOutOfBoundsException) {
			return false
		}

	}

	fun insertAtCursor(editText: EditText, text: String) {
		val oriContent = editText.text.toString()
		val start = editText.selectionStart
		val end = editText.selectionEnd
		if (start >= 0 && end > 0 && start != end) {
			editText.text = editText.text.replace(start, end, text)
		} else {
			val index = if (editText.selectionStart >= 0) editText.selectionStart else 0
			val builder = StringBuilder(oriContent)
			builder.insert(index, text)
			editText.setText(builder.toString())
			editText.setSelection(index + text.length)
		}
	}
}


package ru.fantlab.android.provider.timeline

import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.HapticFeedbackConstants
import android.widget.PopupMenu
import android.widget.TextView
import net.nightwhistler.htmlspanner.HtmlSpanner
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DEFAULT
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.provider.scheme.SchemeParser
import ru.fantlab.android.provider.timeline.handler.*

object HtmlHelper : BetterLinkMovementExtended.OnLinkClickListener, BetterLinkMovementExtended.OnLinkLongClickListener {

	override fun onLongClick(view: TextView, url: String, label: String): Boolean {
		view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
		val menu = PopupMenu(view.context, view)
		menu.setOnMenuItemClickListener { menuItem ->
			when (menuItem.itemId) {
				R.id.copy -> {
					ActivityHelper.copyToClipboard(view.context, if (url.contains("http")) url else "$PROTOCOL_HTTPS://$HOST_DEFAULT$url")
					true
				}
				R.id.open -> {
					SchemeParser.launchUri(view.context, url, label)
					true
				}
				else -> {
					false
				}
			}

		}
		menu.inflate(R.menu.link_popup_menu)
		menu.show()
		return true
	}

	override fun onClick(view: TextView, link: String, label: String): Boolean {
		SchemeParser.launchUri(view.context, link, label)
		return true
	}

	fun htmlIntoTextView(textView: TextView, html: String, width: Int) {
		registerClickEvent(textView)
		textView.text = initHtml(textView, width).fromHtml(format(html).toString())
	}

	private fun registerClickEvent(textView: TextView) {
		val betterLinkMovementMethod = BetterLinkMovementExtended.linkifyHtml(textView)
		betterLinkMovementMethod.setOnLinkClickListener(this)
		betterLinkMovementMethod.setOnLinkLongClickListener(this)
	}

	private fun initHtml(textView: TextView, width: Int): HtmlSpanner {
		val theme = PrefGetter.getThemeType().ordinal
		@ColorInt val windowBackground = getWindowBackground(theme)
		val mySpanner = HtmlSpanner()
		mySpanner.isStripExtraWhiteSpace = true

		mySpanner.registerHandler("u", UnderlineHandler())
		mySpanner.registerHandler("s", StrikethroughHandler())
		mySpanner.registerHandler("q", QuoteHandler(windowBackground))
		mySpanner.registerHandler("h", SpoilerHandler())
		mySpanner.registerHandler("spoiler", SpoilerHandler())
		mySpanner.registerHandler("url", LinkHandler())
		mySpanner.registerHandler("img", DrawableHandler(textView, width))
		mySpanner.registerHandler("list", MarginHandler())
		mySpanner.registerHandler("li", ListsHandler())

		return mySpanner
	}

	@ColorInt
	private fun getWindowBackground(theme: Int): Int {
		return if (theme == PrefGetter.DARK) {
			Color.parseColor("#22252A")
		} else {
			Color.parseColor("#EEEEEE")
		}
	}

	private const val BREAK = "<br>"
	private const val PARAGRAPH_START = "<p>"
	private const val PARAGRAPH_END = "</p>"

	fun format(html: String?): CharSequence {
		if (html == null || html.isEmpty()) return ""
		val formatted = StringBuilder(html)
		if (replace(formatted, PARAGRAPH_START, BREAK)) replace(formatted, PARAGRAPH_END, BREAK)
		trim(formatted)
		return formatted
	}

	private fun replace(input: StringBuilder, from: String, to: String): Boolean {
		var start = input.indexOf(from)
		if (start == -1) return false
		val fromLength = from.length
		val toLength = to.length
		while (start != -1) {
			input.replace(start, start + fromLength, to)
			start = input.indexOf(from, start + toLength)
		}
		return true
	}

	private fun trim(input: StringBuilder) {
		var length = input.length
		val breakLength = BREAK.length
		while (length > 0) {
			if (input.indexOf(BREAK) == 0)
				input.delete(0, breakLength)
			else if (length >= breakLength && input.lastIndexOf(BREAK) == length - breakLength)
				input.delete(length - breakLength, length)
			else if (Character.isWhitespace(input[0]))
				input.deleteCharAt(0)
			else if (Character.isWhitespace(input[length - 1]))
				input.deleteCharAt(length - 1)
			else
				break
			length = input.length
		}
	}
}
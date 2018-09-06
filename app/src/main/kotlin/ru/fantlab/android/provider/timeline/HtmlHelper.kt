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

object HtmlHelper {

	fun htmlIntoTextView(textView: TextView, html: String, width: Int) {
		registerClickEvent(textView)
		textView.text = initHtml(textView, width).fromHtml(html)
	}

	private fun registerClickEvent(textView: TextView) {
		val betterLinkMovementMethod = BetterLinkMovementExtended.linkifyHtml(textView)
		betterLinkMovementMethod.setOnLinkClickListener { view, link, label ->
			SchemeParser.launchUri(view.context, link, label)
			true
		}
		betterLinkMovementMethod.setOnLinkLongClickListener { view, url, label ->
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
			true
		}
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
}
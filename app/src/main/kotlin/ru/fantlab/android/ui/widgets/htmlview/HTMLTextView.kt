package ru.fantlab.android.ui.widgets.htmlview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.ViewTreeObserver
import android.widget.PopupMenu
import android.widget.TextView
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.TypeFaceHelper
import ru.fantlab.android.provider.handler.BetterLinkMovementExtended
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.scheme.SchemeParser
import ru.fantlab.android.ui.widgets.htmlview.drawable.DrawableGetter
import java.util.regex.Matcher
import java.util.regex.Pattern

open class HTMLTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr),
		ViewTreeObserver.OnGlobalLayoutListener,
		BetterLinkMovementExtended.OnLinkClickListener,
		BetterLinkMovementExtended.OnLinkLongClickListener {

	private val sb = SpannableStringBuilder()

	var html: CharSequence? = null
		set(value) {
			field = value
			render(html)
		}

	init {
		init()
	}

	private fun init() {
		if (isInEditMode) return
		viewTreeObserver.addOnGlobalLayoutListener(this)
		TypeFaceHelper.applyTypeface(this)
	}

	override fun onGlobalLayout() {
		viewTreeObserver.removeOnGlobalLayoutListener(this);
		render(text.toString())
	}

	private fun render(html: CharSequence?) {

		if (sb.isEmpty()) {
			parseHtml(sb, html
					?.replace(LIST_TAG.toRegex(), "")
					?.replace(LI_TAG.toRegex(), list)
					?.replace(A_TAG.toRegex(), "[url=$1]$2[/url]")
					?.replace(OLD_TAG.toRegex(), "")
					?.replace(PHOTO_TAG.toRegex(), "")
			)
		}
		text = sb

		registerClickEvent(this)
	}

	private fun parseHtml(sb: SpannableStringBuilder, html: CharSequence?) {
		var lastIndex = 0
		val matcherTag: Matcher = TAG.matcher(html)
		val firstStep = sb.isEmpty()
		while (matcherTag.find()) {
			val start = matcherTag.start(0)
			val end = matcherTag.end(0)
			val tag = matcherTag.group(1)
			val href = matcherTag.group(3)
			val content = matcherTag.group(4)
			var startContent: Int
			var endContent: Int
			if (firstStep) {
				sb.append(html?.substring(lastIndex, start))
				sb.append(content)
				startContent = sb.length - content.length
				endContent = sb.length
			} else {
				startContent = start
				endContent = startContent + content.length
				sb.replace(startContent, end, content)
			}

			when (tag.toLowerCase()) {
				"b" -> {
					sb.setSpan(StyleSpan(Typeface.BOLD), startContent, endContent, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
				}
				"i" -> {
					sb.setSpan(StyleSpan(Typeface.ITALIC), startContent, endContent, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
				}
				"u" -> {
					sb.setSpan(UnderlineSpan(), startContent, endContent, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
				}
				"s" -> {
					sb.setSpan(StrikethroughSpan(), startContent, endContent, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
				}
				"p" -> {
					sb.replace(startContent, endContent, newLine + content)
				}
				"q" -> {
					val theme = PrefGetter.getThemeType().ordinal
					sb.setSpan(CustomQuoteSpan(getWindowBackground(theme)), startContent, endContent, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
				}
				"h",
				"spoiler" -> {
					sb.setSpan(SpoilerSpan(), startContent, endContent, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
				}
				"img" -> {
					sb.replace(startContent, sb.length, "￼")
					val imageGetter = DrawableGetter(this, width)
					sb.setSpan(ImageSpan(imageGetter.getDrawable(content)), startContent, sb.length, Spannable.SPAN_POINT_MARK)
				}
				"link",
				"url" -> {
					if (!href.toString().contains(".jpg"))
						sb.setSpan(LinkSpan(href, linkColor), startContent, endContent, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
					else {
						sb.replace(startContent, sb.length, "￼")
						val imageGetter = DrawableGetter(this, width)
						sb.setSpan(ImageSpan(imageGetter.getDrawable(href)), startContent, sb.length, Spannable.SPAN_POINT_MARK)
					}
				}
				"video" -> {
					sb.setSpan(LinkSpan(href, linkColor), startContent, endContent, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
				}
				"user",
				"award",
				"cycle",
				"edition",
				"work",
				"autor",
				"person",
				"art",
				"dictor",
				"series",
				"film",
				"translator",
				"pub" -> {
					sb.setSpan(LinkSpan("/$tag$href", linkColor), startContent, endContent, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
				}
			}
			lastIndex = end
		}

		if (firstStep) sb.append(html?.substring(lastIndex, html.length))

		val hasTags = TAG.matcher(sb.toString()).find()
		if (hasTags) {
			parseHtml(sb, sb.toString())
			return
		}

		val matcherSmiles: Matcher = SMILES_TAG.matcher(sb.toString())
		while (matcherSmiles.find()) {
			val start = matcherSmiles.start(0)
			val end = matcherSmiles.end(0)
			val tag = matcherSmiles.group(1)
			val imageGetter = DrawableGetter(this, width)
			sb.setSpan(ImageSpan(imageGetter.getDrawable("file:///android_asset/smiles/$tag.gif")), start, end, Spannable.SPAN_POINT_MARK)
		}
	}

	private fun registerClickEvent(textView: TextView) {
		val betterLinkMovementMethod = BetterLinkMovementExtended.linkifyHtml(textView)
		betterLinkMovementMethod.setOnLinkClickListener(this)
		betterLinkMovementMethod.setOnLinkLongClickListener(this)
	}

	override fun onLongClick(view: TextView, link: String, label: String): Boolean {
		view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
		val menu = PopupMenu(view.context, view)
		menu.setOnMenuItemClickListener { menuItem ->
			val url = if (link.contains("http")) link else "${LinkParserHelper.PROTOCOL_HTTPS}://${LinkParserHelper.HOST_DEFAULT}$link"
			when (menuItem.itemId) {
				R.id.copy -> {
					ActivityHelper.copyToClipboard(view.context, url)
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

	private fun getWindowBackground(theme: Int): Int {
		return if (theme == PrefGetter.DARK) {
			Color.parseColor("#22252A")
		} else {
			Color.parseColor("#EEEEEE")
		}
	}

	companion object {
		private val TAG: Pattern = Pattern.compile("\\[([a-zA-Z]+)([=]|)(.*?)](.*?)\\[\\/\\1]", Pattern.DOTALL)
		private val OLD_TAG: Pattern = Pattern.compile("<(.*?)>")
		private val PHOTO_TAG: Pattern = Pattern.compile("\\[PHOTO.*?]")
		private val LIST_TAG: Pattern = Pattern.compile("\\[list\\]|\\[\\/list\\]", Pattern.CASE_INSENSITIVE)
		private val LI_TAG: Pattern = Pattern.compile("\\[\\*\\]")
		private val A_TAG: Pattern = Pattern.compile("<a href=(.*?)>(.*?)<\\/a>")
		private val SMILES_TAG: Pattern = Pattern.compile(":([a-z]+):")
		private val linkColor = Color.parseColor("#4078C0")
		private const val newLine = "\n"
		private const val list = "\t• "
	}


}
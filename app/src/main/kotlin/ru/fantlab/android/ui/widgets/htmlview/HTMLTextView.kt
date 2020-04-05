package ru.fantlab.android.ui.widgets.htmlview

import android.content.Context
import android.text.Html
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SliderModel
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.TypeFaceHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.provider.handler.BetterLinkMovementExtended
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.scheme.SchemeParser
import ru.fantlab.android.ui.widgets.GallerySlider
import ru.fantlab.android.ui.widgets.htmlview.drawable.DrawableGetter

open class HTMLTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr),
		BetterLinkMovementExtended.OnLinkClickListener,
		BetterLinkMovementExtended.OnLinkLongClickListener, ViewTreeObserver.OnGlobalLayoutListener {

	lateinit var betterLinkMovementMethod: BetterLinkMovementExtended

	var html: CharSequence? = null
		set(value) {
			field = value
			render(value)
		}

	init {
		init()
	}

	private fun init() {
		if (isInEditMode) return
		TypeFaceHelper.applyTypeface(this)
		setLinkTextColor(ViewHelper.getSecondaryTextColor(context))
		betterLinkMovementMethod = BetterLinkMovementExtended.linkifyHtml(this)
		betterLinkMovementMethod.setOnLinkClickListener(this)
		betterLinkMovementMethod.setOnLinkLongClickListener(this)
		viewTreeObserver.addOnGlobalLayoutListener(this)
	}

	private fun render(html: CharSequence?) {
		if (html == null) return
		val data = App.processor.process(html).toString()
		text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			Html.fromHtml(data, Html.FROM_HTML_MODE_LEGACY, DrawableGetter(this, width), CustomTagHandler());
		} else {
			Html.fromHtml(data, DrawableGetter(this, width), CustomTagHandler());
		}
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		val action = event.actionMasked
		if (action == MotionEvent.ACTION_CANCEL) {
			betterLinkMovementMethod.isLongPress = false
			return super.onTouchEvent(event)
		}
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

			var x = event.x.toInt()
			var y = event.y.toInt()
			x -= totalPaddingLeft
			y -= totalPaddingTop
			x += scrollX
			y += scrollY

			val layout = layout
			val line = layout.getLineForVertical(y)
			val off = layout.getOffsetForHorizontal(line, x.toFloat())

			val buffer = SpannableString(text)

			val link = buffer.getSpans(off, off, ClickableSpan::class.java)
			if (link.isNotEmpty()) {
				betterLinkMovementMethod.isLongPress = true
				betterLinkMovementMethod.onTouchEvent(this, buffer, event)
				return true
			}
			return super.onTouchEvent(event)
		}

		return false
	}

	override fun onLongClick(view: TextView, link: String, label: String): Boolean {
		view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
		val menu = PopupMenu(view.context, view)
		menu.setOnMenuItemClickListener { menuItem ->
			val url = if (link.contains("http")) link else "${LinkParserHelper.PROTOCOL_HTTPS}://${LinkParserHelper.HOST_DEFAULT}/$link"
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

	override fun onGlobalLayout() {
		viewTreeObserver.removeOnGlobalLayoutListener(this)
		if (lineCount >= maxLines) {
			val lineEndIndex = layout.getLineEnd(maxLines - 1)
			if (length() > lineEndIndex) {
				val text = text.subSequence(0, lineEndIndex - 3).toString() + "..."
				setText(text)
			}
		}
	}

}
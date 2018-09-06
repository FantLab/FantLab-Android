package ru.fantlab.android.provider.timeline.handler

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import net.nightwhistler.htmlspanner.TagNodeHandler
import org.htmlcleaner.TagNode

class SpoilerHandler : TagNodeHandler() {

	override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {

		builder.setSpan(
				SpoilerSpan(),
				start, end, Spannable.SPAN_POINT_MARK)
	}


	class SpoilerSpan : ClickableSpan() {

		var shown = false

		override fun onClick(widget: View) {
			shown = !shown
			widget.invalidate()
		}

		override fun updateDrawState(ds: TextPaint) {
			if (shown) {
				ds.color = Color.GRAY
				ds.bgColor = Color.parseColor("#00EEEEEE")
			} else {
				ds.color = Color.parseColor("#EEEEEE")
				ds.bgColor = Color.parseColor("#EEEEEE")
			}
		}
	}
}
package ru.fantlab.android.provider.timeline.handler

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import net.nightwhistler.htmlspanner.TagNodeHandler
import org.htmlcleaner.TagNode
import ru.fantlab.android.App
import ru.fantlab.android.R

class SpoilerHandler : TagNodeHandler() {

	override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {

		builder.setSpan(
				SpoilerSpan(),
				start, end, Spannable.SPAN_POINT_MARK)
	}


	class SpoilerSpan : ClickableSpan() {

		var shown = false
		var informed = false

		override fun onClick(widget: View) {
			val context = App.instance
			shown = !shown
			if (shown && !informed) {
				Toasty.warning(context, context.getString(R.string.spoiler), Toast.LENGTH_LONG).show()
				informed = true
			}
			widget.invalidate()
		}

		override fun updateDrawState(ds: TextPaint) {
			if (shown) {
				ds.color = Color.GRAY
				ds.bgColor = Color.TRANSPARENT
			} else {
				ds.color = Color.TRANSPARENT
				ds.bgColor = Color.parseColor("#cccccc")
			}
		}
	}
}
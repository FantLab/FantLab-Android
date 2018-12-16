package ru.fantlab.android.provider.timeline.handler

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import net.nightwhistler.htmlspanner.TagNodeHandler
import org.htmlcleaner.TagNode
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.timeline.handler.drawable.DrawableGetter

class DrawableHandler(private val textView: TextView?, private val width: Int) : TagNodeHandler() {

	private val isNull: Boolean
		get() = textView == null

	override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {
		val src = node.getAttributeByName("src")
		if (!InputHelper.isEmpty(src)) {
			builder.append("￼")
			if (isNull) return
			val imageGetter = DrawableGetter(textView!!, width)
			builder.setSpan(ImageSpan(imageGetter.getDrawable(src.toString())), start, builder.length, Spannable.SPAN_POINT_MARK)
		}
	}
}
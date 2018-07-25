package ru.fantlab.android.provider.timeline.handler

import android.support.annotation.ColorInt
import android.text.SpannableStringBuilder

import net.nightwhistler.htmlspanner.TagNodeHandler

import org.htmlcleaner.TagNode

import ru.fantlab.android.ui.widgets.markdown.style.MarkDownQuoteSpan


class QuoteHandler(@field:ColorInt private val color: Int) : TagNodeHandler() {

	override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {
		builder.setSpan(MarkDownQuoteSpan(color), start, end, 33)
	}
}

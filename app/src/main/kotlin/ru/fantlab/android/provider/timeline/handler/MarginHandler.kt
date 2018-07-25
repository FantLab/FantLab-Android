package ru.fantlab.android.provider.timeline.handler

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan

import net.nightwhistler.htmlspanner.TagNodeHandler

import org.htmlcleaner.TagNode

class MarginHandler : TagNodeHandler() {

	override fun beforeChildren(node: TagNode?, builder: SpannableStringBuilder?) {
		if (builder!!.isNotEmpty() && builder[builder.length - 1].toInt() != 10) { //'10 = \n'
			this.appendNewLine(builder)
		}
	}

	override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {
		builder.setSpan(LeadingMarginSpan.Standard(30), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
		this.appendNewLine(builder)
	}
}

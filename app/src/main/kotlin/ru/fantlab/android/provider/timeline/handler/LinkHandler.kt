package ru.fantlab.android.provider.timeline.handler

import android.graphics.Color
import android.text.SpannableStringBuilder

import net.nightwhistler.htmlspanner.TagNodeHandler

import org.htmlcleaner.TagNode

import ru.fantlab.android.ui.widgets.markdown.style.LinkSpan

class LinkHandler : TagNodeHandler() {

	override fun handleTagNode(node: TagNode, spannableStringBuilder: SpannableStringBuilder, start: Int, end: Int) {
		val href = node.getAttributeByName("href")
		if (href != null) {
			spannableStringBuilder.setSpan(LinkSpan(href, linkColor), start, end, 33)
		} else if (node.text != null) {
			spannableStringBuilder.setSpan(LinkSpan("https://fantlab.ru/user" + node.text.toString(), linkColor), start, end, 33)
		}
	}

	companion object {
		private val linkColor = Color.parseColor("#4078C0")
	}
}

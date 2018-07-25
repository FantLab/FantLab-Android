package ru.fantlab.android.provider.timeline.handler

import android.text.SpannableStringBuilder
import net.nightwhistler.htmlspanner.TagNodeHandler
import org.htmlcleaner.TagNode

class ListsHandler : TagNodeHandler() {

	private fun getParentName(node: TagNode): String? {
		return if (node.parent == null) null else node.parent.name
	}

	override fun beforeChildren(node: TagNode, builder: SpannableStringBuilder?) {
		if ("list" == getParentName(node)) {
				builder!!.append("\u2022 ")
		}
	}

	override fun handleTagNode(tagNode: TagNode, spannableStringBuilder: SpannableStringBuilder, i: Int, i1: Int) {
		appendNewLine(spannableStringBuilder)
	}

}
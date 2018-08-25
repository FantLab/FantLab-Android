package ru.fantlab.android.provider.markdown.extension.innertags.internal

import org.commonmark.node.*
import org.commonmark.parser.PostProcessor
import java.util.regex.Matcher
import java.util.regex.Pattern

class InnerTagPostProcessor : PostProcessor {

	companion object {
		private val TAG = Pattern.compile("\\[([a-zA-Z]+)([=]|)(.*?)](.*?)\\[.*?]")
	}

	override fun process(node: Node): Node {
		val innertagVisitor = InnerTagVisitor()
		node.accept(innertagVisitor)
		return node
	}

	private fun linkify(text: Text) {
		val literal = text.literal.replace(":[а-я_А-Я]+]".toRegex(), "]")
		val matcher: Matcher = TAG.matcher(literal)
		var lastNode: Node = text
		var last = 0
		while (matcher.find()) {
			val type = matcher.group(1)
			val url = matcher.group(3)
			val caption = matcher.group(4)
			val startTag = matcher.start()
			val endTag = matcher.end()
			val contentNode = Text(literal.substring(last, startTag))
			val contentTitle = Text(caption)
			var customNode: Node?

			when (type){
				"img" -> {
					customNode = Image(caption, null)
				}
				else ->{
					customNode = Link("/$type$url", null)
				}
			}

			lastNode = insertNode(contentNode, lastNode)
			customNode.appendChild(contentTitle)
			lastNode = insertNode(customNode, lastNode)

			last = endTag
		}
		if (last != literal.length) {
			insertNode(Text(literal.substring(last)), lastNode)
		}
		text.unlink()
	}

	private fun insertNode(node: Node, insertAfterNode: Node): Node {
		insertAfterNode.insertAfter(node)
		return node
	}

	private inner class InnerTagVisitor : AbstractVisitor() {
		internal var inLink = 0

		override fun visit(link: org.commonmark.node.Link) {
			inLink++
			super.visit(link)
			inLink--
		}

		override fun visit(text: Text) {
			if (inLink == 0) {
				linkify(text)
			}
		}
	}
}

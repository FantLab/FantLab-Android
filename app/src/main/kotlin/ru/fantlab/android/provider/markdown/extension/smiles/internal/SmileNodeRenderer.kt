package ru.fantlab.android.provider.markdown.extension.smiles.internal

import org.commonmark.node.Node
import org.commonmark.renderer.NodeRenderer
import org.commonmark.renderer.html.HtmlNodeRendererContext
import org.commonmark.renderer.html.HtmlWriter
import ru.fantlab.android.provider.markdown.extension.smiles.Smile

class SmileNodeRenderer(private val context: HtmlNodeRendererContext) : NodeRenderer {
	private val html: HtmlWriter

	init {
		this.html = context.writer
	}

	override fun getNodeTypes(): Set<Class<out Node>> {
		return setOf<Class<out Node>>(Smile::class.java)
	}

	override fun render(node: Node) {
		html.raw("<img src=\"file:///android_asset/smiles/")
		renderChildren(node)
		html.raw(".gif\"/>")
	}

	private fun renderChildren(parent: Node) {
		var node: Node? = parent.firstChild
		while (node != null) {
			val next = node.next
			context.render(node)
			node = next
		}
	}
}
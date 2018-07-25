package ru.fantlab.android.provider.markdown.extension.smiles

import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

import ru.fantlab.android.provider.markdown.extension.smiles.internal.SmileDelimiterProcessor
import ru.fantlab.android.provider.markdown.extension.smiles.internal.SmileNodeRenderer

class SmileExtension private constructor() : Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

	override fun extend(parserBuilder: Parser.Builder) {
		parserBuilder.customDelimiterProcessor(SmileDelimiterProcessor())
	}

	override fun extend(rendererBuilder: HtmlRenderer.Builder) {
		rendererBuilder.nodeRendererFactory { context -> SmileNodeRenderer(context) }
	}

	companion object {

		fun create(): Extension {
			return SmileExtension()
		}
	}
}
package ru.fantlab.android.provider.markdown.extension.innertags

import org.commonmark.Extension
import org.commonmark.parser.Parser

import ru.fantlab.android.provider.markdown.extension.innertags.internal.InnerTagPostProcessor

class InnerTagExtension private constructor() : Parser.ParserExtension {

	override fun extend(parserBuilder: Parser.Builder) {
		parserBuilder.postProcessor(InnerTagPostProcessor())
	}

	companion object {

		fun create(): Extension {
			return InnerTagExtension()
		}
	}

}

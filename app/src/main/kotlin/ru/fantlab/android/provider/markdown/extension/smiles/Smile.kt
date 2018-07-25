package ru.fantlab.android.provider.markdown.extension.smiles

import org.commonmark.node.CustomNode
import org.commonmark.node.Delimited

class Smile : CustomNode(), Delimited {

	override fun getOpeningDelimiter(): String {
		return DELIMITER
	}

	override fun getClosingDelimiter(): String {
		return DELIMITER
	}

	companion object {

		private val DELIMITER = ":"
	}
}
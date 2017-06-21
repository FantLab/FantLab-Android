package org.odddev.fantlab.edition

class Edition (
		val content: List<String>,
		val copies: Int,
		val correctLevel: Float,
		val covertType: String,
		val creators: Map<String, List<Creator>>,
		val description: String,
		val id: Int,
		val types: List<String>,
		val format: String,
		val images: List<Picture>,
		val isbns: List<String>,
		val language: Language,
		val notes: String,
		val origName: String,
		val pages: Int,
		val plan: Plan,
		val series: String,
		val type: Int,
		val year: Int) {

	class Creator(
			val id: Int,
			val isOpened: Boolean,
			val name: String,
			val type: String
	)

	class Language(
			val code: String,
			val name: String
	)

	class Plan(
			val date: String,
			val description: String
	)

	class Picture(
			val check: Boolean,
			val image: String,
			val preview: String,
			val copyright: String,
			val orig: Boolean,
			val text: String,
			val type: Int
	)
}
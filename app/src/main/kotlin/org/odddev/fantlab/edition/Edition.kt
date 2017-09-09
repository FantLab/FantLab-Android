package org.odddev.fantlab.edition

import android.support.annotation.Keep

@Keep
class Edition (
		val autors: String = "", //shit in 'work' api [see 'creators']
		val compiers: String = "", // -//-
		val translators: String = "", // -//-
		val content: List<String> = ArrayList<String>(),
		val copies: Int = -1,
		val correctLevel: Float,
		val covertType: String,
		val ebook: Boolean = false,
		val creators: Map<String, List<Creator>> = HashMap<String, List<Edition.Creator>>(),
		val description: String = "",
		val id: Int,
		val types: List<String> = ArrayList<String>(),
		val format: String = "",
		val images: List<Picture> = ArrayList<Edition.Picture>(),
		val isbns: List<String>,
		val language: Language,
		val notes: String = "",
		val origName: String,
		val pages: Int = -1,
		val plan: Plan = Edition.Plan("", ""),
		val series: String = "",
		val type: Int,
		val year: Int) {

	class Creator(
			val id: Int,
			val isOpened: Boolean,
			val name: String,
			val type: String
	)

	class Language(
			val id: Int = -1,
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
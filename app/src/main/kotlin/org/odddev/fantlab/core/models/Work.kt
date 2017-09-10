package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class Work(
		val id: Int,
		val autors: List<String>? = null, // a la "[link=https://fantlab.ru/autor$id]${name}[/link]".formatText()
		val midmark: Float? = null,
		val rating: Float? = null,
		val responseCount: Int? = null,
		val voters: Int? = null,
		val description: String? = null,
		val name: String? = null,
		val nameAlt: String? = null,
		val nameOrig: String? = null,
		val nameBonus: String? = null,
		val notFinished: Boolean? = null,
		val published: Boolean? = null,
		val preparing: Boolean? = null, // в планах
		val type: Int? = null, // work_type_id
		val year: Int? = null,
		val writeYear: Int? = null,
		val deep: Int? = null,
		val plus: Boolean? = null,
		val canDownload: Boolean? = null,
		val hasLp: Boolean? = null, // есть лингвопрофиль
		val forChildren: Boolean? = null
)
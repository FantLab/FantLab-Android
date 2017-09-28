package org.odddev.fantlab.core.models

data class WorkAuthor(
		// id
		val authorId: Int,
		// type (тип автора для данного произведения; автор в произведении может оказаться художником)
		val role: String,   // по-хорошему должен быть Int из набора типов
		val index: Int
)
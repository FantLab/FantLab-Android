package ru.fantlab.android.old.core.models

import android.support.annotation.Keep

@Keep
data class WorkAuthor(
		val workId: Int? = null,
		// id
		val authorId: Int,
		// type (тип автора для данного произведения; автор в произведении может оказаться художником)
		val role: String,   // по-хорошему должен быть Int из набора типов
		val position: Int
)
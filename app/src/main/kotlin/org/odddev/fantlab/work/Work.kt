package org.odddev.fantlab.work

import android.support.annotation.Keep
import org.odddev.fantlab.autors.Autor
import org.odddev.fantlab.autors.autor.AutorFull
import org.odddev.fantlab.edition.Edition

@Keep
class Work(
		val autors: List<Autor>,
		val awards: List<AutorFull.Award>,
		val children: List<AutorFull.Work>,
		val editions: List<Edition>,
		val stat: AutorFull.Stat,
		val saga: List<String>, // входит в ...
		val rating: Rating,
		val responseCount: Int,
		val id: Int,
		val name: String,
		val nameBonus: String,
		val nameOrig: String,
		val description: Description,
		val notes: String,
		val notFinished: Boolean,
		val parent: Boolean,
		val preparing: Boolean,
		val type: Int,
		val year: Int,
		val canDownload: Boolean) { // + classificatory, translations

	class Rating(
			val rating: Float,
			val voters: Int
	)

	class Description(
			val text: String,
			val autor: String
	)
}
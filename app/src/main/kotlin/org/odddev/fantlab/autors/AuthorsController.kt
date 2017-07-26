package org.odddev.fantlab.autors

import com.airbnb.epoxy.TypedEpoxyController
import org.odddev.fantlab.AutorItemBindingModel_
import org.odddev.fantlab.LetterItemBindingModel_

class AuthorsController(private val listener: IAuthorsActions) : TypedEpoxyController<List<Autor>>() {

	override fun buildModels(authors: List<Autor>) {
		var previousLetter = ""
		for (author in authors) {
			val currentLetter = author.nameShort.first().toString()
			LetterItemBindingModel_()
					.id(author.hashCode(), currentLetter.hashCode())
					.letter(currentLetter)
					.addIf(currentLetter != previousLetter, this)
			previousLetter = currentLetter
			AutorItemBindingModel_()
					.id(author.hashCode())
					.autor(author)
					.listener(listener)
					.addTo(this)
		}
	}
}
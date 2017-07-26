package org.odddev.fantlab.autors

import com.airbnb.epoxy.TypedEpoxyController
import org.odddev.fantlab.AutorItemBindingModel_
import org.odddev.fantlab.LetterItemBindingModel_

class AuthorsController : TypedEpoxyController<List<Autor>>() {

	override fun buildModels(authors: List<Autor>) {
		var previousLetter = ""
		for (author in authors) {
			val currentLetter = author.nameShort.first().toString()
			if (currentLetter != previousLetter) {
				previousLetter = currentLetter
				LetterItemBindingModel_()
						.id(previousLetter.hashCode())
						.letter(previousLetter)
						.addTo(this)
			}
			AutorItemBindingModel_()
					.id(author.hashCode())
					.autor(author)
					.addTo(this)
		}
	}
}
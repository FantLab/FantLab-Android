package org.odddev.fantlab.autors.autor

import com.airbnb.epoxy.TypedEpoxyController
import org.odddev.fantlab.AuthorBiographyItemBindingModel_

class AuthorController(private val listener: IAutorActions) : TypedEpoxyController<AutorFull>() {

	override fun buildModels(author: AutorFull) {
		AuthorBiographyItemBindingModel_()
				.id(author.hashCode())
				.autor(author)
				.handler(listener)
				.addTo(this)
	}
}
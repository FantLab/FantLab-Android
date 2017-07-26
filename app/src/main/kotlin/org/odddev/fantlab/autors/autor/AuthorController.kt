package org.odddev.fantlab.autors.autor

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import org.odddev.fantlab.AuthorAwardsHeaderItemBindingModel_
import org.odddev.fantlab.AuthorAwardsItemBindingModel_
import org.odddev.fantlab.AuthorBiographyFooterItemBindingModel_
import org.odddev.fantlab.AuthorBiographyItemBindingModel_

class AuthorController(private val listener: IAutorActions) : TypedEpoxyController<AutorFull>() {

	@AutoModel
	lateinit var biographyModel: AuthorBiographyItemBindingModel_

	@AutoModel
	lateinit var biographyFooter: AuthorBiographyFooterItemBindingModel_

	@AutoModel
	lateinit var awardsHeader: AuthorAwardsHeaderItemBindingModel_

	override fun buildModels(author: AutorFull) {
		biographyModel
				.biography(author.biography.anons)
				.addTo(this)
		biographyFooter
				.handler(listener)
				.addTo(this)
		awardsHeader
				.count(author.awards.size)
				.handler(listener)
				.addIf(author.awards.isNotEmpty(), this)
		val awards = author.awards.reversed()
		(0..2)
				.map { awards.getOrNull(it) }
				.forEach {
					it?.let {
						AuthorAwardsItemBindingModel_()
								.id(it.hashCode())
								.award(it)
								.addTo(this)
					}
				}
	}
}
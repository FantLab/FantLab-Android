package org.odddev.fantlab.autors.autor

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import org.odddev.fantlab.*

class AuthorController(private val listener: IAutorActions) : TypedEpoxyController<AutorFull>() {

	@AutoModel
	lateinit var biographyModel: AuthorBiographyItemBindingModel_

	@AutoModel
	lateinit var biographyFooter: AuthorBiographyFooterItemBindingModel_

	@AutoModel
	lateinit var awardsHeader: AuthorAwardsHeaderItemBindingModel_

	@AutoModel
	lateinit var worksHeader: AuthorWorksHeaderItemBindingModel_

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

		val worksCount = (0..author.works.size() - 1).sumBy { author.works[author.works.keyAt(it)].size }
		worksHeader
				.count(worksCount)
				.handler(listener)
				.addTo(this)

		val allWorks = ArrayList<AutorFull.Work>()
		for (i in (0..author.works.size() - 1)) {
			allWorks.addAll(author.works[author.works.keyAt(i)])
		}
		// todo заменить it.midmark * it.voters на рейтинг
		val best = allWorks.sortedByDescending { it.midmark * it.voters }.distinctBy { it.id }.take(3)
		(0..2)
				.map { best[it] }
				.forEach {
					it.let {
						AuthorWorksItemBindingModel_()
								.id(it.hashCode())
								.work(it)
								.addTo(this)
					}
				}
	}
}
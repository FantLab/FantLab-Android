package org.odddev.fantlab.autors.autor

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import org.odddev.fantlab.*

class AuthorController(private val handler: IAutorActions) : Typed2EpoxyController<AutorFull?, Boolean>() {

	@AutoModel
	lateinit var biography: AuthorBiographyItemBindingModel_

	@AutoModel
	lateinit var biographyFooter: AuthorBiographyFooterItemBindingModel_

	@AutoModel
	lateinit var awardsHeader: AuthorAwardsHeaderItemBindingModel_

	@AutoModel
	lateinit var worksHeader: AuthorWorksHeaderItemBindingModel_

	@AutoModel
	lateinit var loading: LoadingItemBindingModel_

	override fun buildModels(author: AutorFull?, loadingMore: Boolean) {
		DividerItemBindingModel_()
				.id(0)
				.addTo(this)

		loading
				.addIf(author == null && loadingMore, this)

		author ?: return

		biography
				.biography(author.biography.anons)
				.addTo(this)
		biographyFooter
				.handler(handler)
				.addTo(this)

		DividerItemBindingModel_()
				.id(1)
				.addTo(this)

		awardsHeader
				.count(author.awards.size)
				.handler(handler)
				.addIf(author.awards.isNotEmpty(), this)

		val awards = author.awards
				.reversed()
		(0..2)
				.map { awards.getOrNull(it) }
				.forEach {
					it?.let {
						AuthorAwardsItemBindingModel_()
								.id(it.hashCode())
								.award(it)
								.handler(handler)
								.addTo(this)
					}
				}

		val allWorks = ArrayList<AutorFull.Work>()
		for (i in (0..author.works.size() - 1)) {
			allWorks.addAll(author.works[author.works.keyAt(i)])
		}

		DividerItemBindingModel_()
				.id(2)
				.addTo(this)

		val authorWorks = allWorks
				.distinctBy { it.id }
				.filter { it.autors.any { it.id == author.id } }
				.sortedByDescending { it.rating }

		worksHeader
				.count(authorWorks.size)
				.handler(handler)
				.addTo(this)

		(0..2)
				.map { authorWorks.getOrNull(it) }
				.forEach {
					it?.let {
						AuthorWorksItemBindingModel_()
								.id(it.hashCode())
								.work(it)
								.handler(handler)
								.addTo(this)
					}
				}

		DividerItemBindingModel_()
				.id(3)
				.addTo(this)

		loading
				.addIf(loadingMore, this)
	}
}
package org.odddev.fantlab.autors.autor

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import org.odddev.fantlab.*

class AuthorController(private val listener: IAutorActions) : Typed2EpoxyController<AutorFull?, Boolean>() {

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
		loading
				.addIf(author == null && loadingMore, this)

		author ?: return

		DividerItemBindingModel_()
				.id(0)
				.addTo(this)

		biography
				.biography(author.biography.anons)
				.addTo(this)
		biographyFooter
				.handler(listener)
				.addTo(this)

		DividerItemBindingModel_()
				.id(1)
				.addTo(this)

		awardsHeader
				.count(author.awards.size)
				.handler(listener)
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
				.handler(listener)
				.addTo(this)

		(0..2)
				.map { authorWorks.getOrNull(it) }
				.forEach {
					it?.let {
						AuthorWorksItemBindingModel_()
								.id(it.hashCode())
								.work(it)
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
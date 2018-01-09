package ru.fantlab.android.edition

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.core.db.MainDatabase
import ru.fantlab.android.core.di.Injector
import ru.fantlab.android.core.network.IServerApi
import javax.inject.Inject

class EditionProvider : IEditionProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getEdition(id: Int): Flowable<Edition> {
		serverApi.getAuthor(id)
				.subscribeOn(Schedulers.io())
				.subscribe {
					response -> run {
						database.authorDao().saveAuthorFromResponse(response)
						database.authorPseudonymDao().saveAuthorPseudonymsFromResponse(response)
						database.authorStatDao().saveAuthorStatFromResponse(response)
						database.authorDao().saveWorksAuthorsFromResponse(response)
					// todo аналогично сохранить художников
					// todo сохранить ворки
					// todo сохранить связи ворков друг с другом. вроде все, можно отрисовывать
					}
				}
		return null!!/*database.authorDao()
				.getAsFlowable(id)
				.distinctUntilChanged()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())*/
	}
}

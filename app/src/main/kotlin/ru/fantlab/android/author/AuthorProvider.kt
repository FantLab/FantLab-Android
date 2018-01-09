package ru.fantlab.android.author

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.author.models.Author
import ru.fantlab.android.core.db.MainDatabase
import ru.fantlab.android.core.di.Injector
import ru.fantlab.android.core.network.IServerApi
import javax.inject.Inject

class AuthorProvider : IAuthorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAuthor(id: Int): Flowable<Author> {
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
		return database.authorDao()
				.getAsFlowable(id)
				.distinctUntilChanged()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}
}

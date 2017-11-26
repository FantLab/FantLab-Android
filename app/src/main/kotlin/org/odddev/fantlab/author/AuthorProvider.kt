package org.odddev.fantlab.author

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.db.MainDatabase
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorProvider : IAuthorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	/**
	 * После получения данных алгоритм такой:
	 * 1. Для каждой записи из модели считываем из таблицы уже существующую запись с таким же primary_key
	 * (если уже загрузили, берем из памяти)
	 * 2. Поочередно сравниваем записи и выбираем наиболее полный/актуальный вариант
	 * 3. Записываем актуальный результат в базу
	 * 4. Считываем из базы записи, необходимые для отображения информации на экране
	 */
	override fun getAuthor(id: Int): Flowable<Void> =
			serverApi.getAuthor(id)
					.flatMap { response -> database.authorDao().saveAuthorFromResponse(response) }
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
}

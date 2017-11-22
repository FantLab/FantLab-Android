package org.odddev.fantlab.author

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorProvider : IAuthorProvider {

	@Inject
	lateinit var serverApi: IServerApi

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
	override fun getAuthor(id: Int): Observable<Void> =
			serverApi.getAuthor(id)
					.flatMap { authorPageInfo -> null as Observable<Void>/*writeToDb(authorPageInfo)*/ }
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
}

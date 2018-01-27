package ru.fantlab.android.provider

import io.reactivex.Observable
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.data.service.CommonRestService

object StubProvider : CommonRestService {

	override fun getNews(page: Int): Observable<Pageable<News>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}
}
package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.News

interface CommonRestService {

	@GET("news.json")
	fun getNews(@Query("page") page: Int): Observable<Pageable<News>>
}
package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.ForumMessage
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.data.dao.model.Response

interface CommonRestService {

	@GET("news.json")
	fun getNews(@Query("page") page: Int): Observable<Pageable<News>>

	@GET("responses")
	fun getResponses(@Query("page") page: Int): Observable<Pageable<Response>>

	@GET("feed.json")
	fun getForumFeed(@Query("page") page: Int): Observable<Pageable<ForumMessage>>
}
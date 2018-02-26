package ru.fantlab.android.provider

import io.reactivex.Observable
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.UserId
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.data.service.CommonRestService
import ru.fantlab.android.data.service.UserRestService

object StubProvider : CommonRestService, UserRestService {

	override fun getNews(page: Int): Observable<Pageable<News>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}

	override fun getResponses(page: Int): Observable<Pageable<Response>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}

	override fun getForumFeed(page: Int): Observable<Pageable<ForumMessage>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}

	override fun getUser(id: Int): Observable<User> {
		return Observable.just(null)
	}

	override fun getLoggedUser(id: Int): Observable<Login> {
		return Observable.just(null)
	}

	override fun getUserId(login: String): Observable<UserId> {
		return Observable.just(UserId(1))
	}

	override fun getMarks(id: Int, page: Int): Observable<Pageable<UserMark>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}

	override fun getResponses(id: Int, page: Int): Observable<Pageable<Response>> {
		return Observable.just(Pageable(last = 1, items = ArrayList()))
	}
}
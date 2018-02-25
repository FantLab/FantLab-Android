package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import ru.fantlab.android.data.dao.model.AuthorInList

interface AuthorRestService {

	@GET("autorsall")
	fun getList(): Observable<List<AuthorInList>>
}
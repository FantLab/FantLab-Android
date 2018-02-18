package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.UserId
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.data.dao.model.UserMark

interface UserRestService {

	@GET("userlogin/{login}")
	fun getUserId(@Path(value = "login") login: String): Observable<UserId>

	@GET("user/{id}")
	fun getLoggedUser(@Path(value = "id") id: Int): Observable<Login>

	@GET("user/{id}")
	fun getUser(@Path(value = "id") id: Int): Observable<User>

	@GET("user/{id}/marks")
	fun getMarks(
			@Path(value = "id") id: Int,
			@Query(value = "page") page: Int = 1
	): Observable<Pageable<UserMark>>

	@GET("user/{id}/responses")
	fun getResponses(
			@Path(value = "id") id: Int,
			@Query(value = "page") page: Int = 1
	): Observable<Pageable<Response>>
}
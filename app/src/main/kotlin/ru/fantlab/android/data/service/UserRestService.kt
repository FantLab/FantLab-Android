package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.data.dao.model.UserMark

interface UserRestService {

	@GET("user/{id}")
	fun getUser(@Path(value = "id") id: Int) : Observable<User?>

	// todo удалить после появления в API метода получения инфы о юзере по логину
	@GET("user/{id}")
	fun getLoggedUser(@Path(value = "id") id: Int) : Observable<Login>

	@GET("user/{login}")
	fun getLoggedUser(@Path(value = "login") login: String) : Observable<Login>

	@GET("user/{id}/marks")
	fun getMarks(
			@Path(value = "id") id: Int,
			@Query(value = "page") page: Int
	) : Observable<Pageable<UserMark>>
}
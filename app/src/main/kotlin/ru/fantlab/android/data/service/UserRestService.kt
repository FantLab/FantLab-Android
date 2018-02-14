package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import ru.fantlab.android.data.dao.model.Login

interface UserRestService {

	@GET("user/{id}?1")
	fun getUser(@Path(value = "id") id: Int) : Observable<Login>

	@GET("user/{login}")
	fun getUser(@Path(value = "login") login: String) : Observable<Login>
}
package ru.fantlab.android.data.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRestService {

	@FormUrlEncoded
	@POST("login")
	fun login(
			@Field("login") login: String,
			@Field("password") password: String
	): Observable<Response<ResponseBody>>
}
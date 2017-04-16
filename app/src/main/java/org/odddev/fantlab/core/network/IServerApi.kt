package org.odddev.fantlab.core.network

import okhttp3.ResponseBody
import org.odddev.fantlab.autors.AutorsResponse
import org.odddev.fantlab.award.Award
import retrofit2.Response
import retrofit2.http.*
import rx.Single

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

interface IServerApi {

	@FormUrlEncoded
	@POST("/login")
	fun login(@Field("login") login: String,
			  @Field("password") password: String): Single<Response<ResponseBody>>

	@GET("/awards.json")
	fun getAwards(): Single<List<Award>>

	@GET("/award{id}.json")
	fun getAward(@Path("id") id: Int): Single<Award>

	@GET("/autorsall.json")
	fun getAutors(): Single<AutorsResponse>
}

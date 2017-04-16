package org.odddev.fantlab.core.network

import org.odddev.fantlab.autors.AutorsResponse
import org.odddev.fantlab.award.Award
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

interface IServerApi {

	@GET("/awards.json")
	fun getAwards(): Single<List<Award>>

	@GET("/award{id}.json")
	fun getAward(@Path("id") id: Int): Single<Award>

	@GET("/autorsall.json")
	fun getAutors(): Single<AutorsResponse>
}

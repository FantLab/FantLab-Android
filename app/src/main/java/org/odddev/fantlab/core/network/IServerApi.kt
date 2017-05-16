package org.odddev.fantlab.core.network

import io.reactivex.Observable
import org.odddev.fantlab.autors.AutorsResponse
import org.odddev.fantlab.award.Award
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

interface IServerApi {

	@GET("/awards.json")
	fun getAwards(): Observable<List<Award>>

	@GET("/award{id}.json")
	fun getAward(@Path("id") id: Int): Observable<Award>

	@GET("/autorsall.json")
	fun getAutors(): Observable<AutorsResponse>
}

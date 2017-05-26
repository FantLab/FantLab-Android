package org.odddev.fantlab.core.network

import io.reactivex.Observable
import org.odddev.fantlab.autors.AutorsResponse
import org.odddev.fantlab.autors.autor.AutorFull
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

	@GET("/autor{id}.json?biblio_blocks=1&awards=1&la_resume=1&userprofile=1&biography=1")
	fun getAutor(@Path("id") id: Int): Observable<AutorFull>

	@GET("/autor{id}/alleditions.json?editions_blocks=1")
	fun getAutorEditions(@Path("id") id: Int): Observable<Unit>

	@GET("/autor{id}/responses.json?page={page}")
	fun getAutorResponses(@Path("id") id: Int, @Path("page") page: Int): Observable<Unit>

	@GET("/work{id}?translations=1&classificatory=1&children=1&parents=1&awards=1&films=1&editions=1")
	fun getWork(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id}/responses.json")
	fun getWorkResponses(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id}/analogs")
	fun getWorkAnalogs(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id_1}/ajaxsetmark{mark}towork{id_2}")
	fun setWorkMark(@Path("id_1") idFrom: Int, @Path("mark") mark: Int, @Path("id_2") idTo: Int): Observable<Unit>

	@GET("/work{work_id}/analog{analog_id}/add")
	fun addWorkAnalog(@Path("work_id") workId: Int, @Path("analog_id") analogId: Int): Observable<Unit>

	@GET("/work{work_id}/analog{analog_id}/remove")
	fun removeWorkAnalog(@Path("work_id") workId: Int, @Path("analog_id") analogId: Int): Observable<Unit>

	@GET("/bookcaseclick{work_id}bc{bookcase_id}change{to_add}")
	fun editWorkAtBookcase(@Path("work_id") workId: Int, @Path("bookcase_id") bookcaseId: Int, @Path("to_add") toAdd: Boolean): Observable<Unit>

	@GET("/vote{id}{to_vote}") // to_vote = plus/minus
	fun voteForResponse(@Path("id") responseId: Int, @Path("to_vote") toVote: String): Observable<Unit>

	@GET("/edition{id}.json")
	fun getEdition(@Path("id") id: Int): Observable<Unit>

	@GET("/bookcaseclick{edition_id}bc{bookcase_id}change{to_add}")
	fun editEditionAtBookcase(@Path("edition_id") editionId: Int, @Path("bookcase_id") bookcaseId: Int, @Path("to_add") toAdd: Boolean): Observable<Unit>
}

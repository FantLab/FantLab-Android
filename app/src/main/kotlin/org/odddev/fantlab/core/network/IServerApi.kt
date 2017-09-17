package org.odddev.fantlab.core.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import org.odddev.fantlab.autors.AuthorsResponse
import org.odddev.fantlab.autors.autor.AutorFull
import org.odddev.fantlab.award.Award
import retrofit2.Response
import retrofit2.http.*

interface IServerApi {

	@FormUrlEncoded
	@POST("/login")
	fun login(@Field("login") login: String,
			  @Field("password") password: String): Single<Response<ResponseBody>>

	// includeNonFantastic = 0/1
	@GET("/awards.json")
	fun getAwards(@Query("nonfant") includeNonFantastic: Int): Observable<List<Award>>

	@GET("/award{id}.json")
	fun getAward(@Path("id") id: Int,
				 @Query("include_contests") includeContests: Int,
				 @Query("include_nomi") includeNominations: Int): Observable<Award>

	@GET("/contest{id}.json")
	fun getContest(@Path("id") id: Int,
				   @Query("include_works") includeWorks: Int): Observable<Unit>

	@GET("/autorsall")
	fun getAuthors(): Observable<AuthorsResponse>

	@GET("/autor/{id}/extended")
	fun getAuthor(@Path("id") id: Int): Observable<AutorFull>

	@GET("/autor{id}/alleditions.json")
	fun getAuthorEditions(@Path("id") id: Int,
						 @Query("editions_blocks") includeEditionBlocks: Int): Observable<Unit>

	@GET("/autor{id}/responses.json")
	fun getAuthorResponses(@Path("id") id: Int,
						  @Query("page") page: Int): Observable<Unit>

	@GET("/work/{id}/extended")
	fun getWork(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id}/responses.json")
	fun getWorkResponses(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id}/analogs")
	fun getWorkAnalogs(@Path("id") id: Int): Observable<Unit>

	@GET("/work{id_1}/ajaxsetmark{mark}towork{id_2}")
	fun setWorkMark(@Path("id_1") idFrom: Int,
					@Path("mark") mark: Int,
					@Path("id_2") idTo: Int): Observable<Unit>

	// action = add/remove
	@GET("/work{work_id}/analog{analog_id}/{action}")
	fun editWorkAnalog(@Path("work_id") workId: Int,
					   @Path("analog_id") analogId: Int,
					   @Path("action") action: String): Observable<Unit>

	@GET("/workclassif{id}")
	fun getWorkClassification(@Path("id") workId: Int): Observable<Unit>

	@GET("/genrevote{id}?{vote}")
	fun editWorkClassification(@Path("id") workId: Int,
							   @Path("vote") voteQuery: String): Observable<Unit>

	// action = plus/minus
	@GET("/vote{id}{action}")
	fun voteForResponse(@Path("id") responseId: Int,
						@Path("action") action: String): Observable<Unit>

	@GET("/edition/{id}/extended")
	fun getEdition(@Path("id") id: Int,
				   @Query("include_content") includeContent: Int,
				   @Query("images_plus") includeAdditionalImages: Int): Observable<Unit>

	// c1, c2 divider = `:`
	@GET("/compare")
	fun compareEditions(@Query("c1") firstSet: String,
						@Query("c2") secondSet: String): Observable<Unit>

	@GET("/searchmain")
	fun search(@Query("searchstr") query: String,
			   @Query("page") page: Int): Observable<Unit>

	// type = autors/works/editions/films/articles/persons/awards
	@GET("/search-{type}.json")
	fun searchByType(@Path("type") type: String,
					 @Query("q") query: String,
					 @Query("page") page: Int): Observable<Unit>

	@GET("/user{id}/responses.json")
	fun getUserResponses(@Path("id") userId: Int,
						 @Query("page") page: Int): Observable<Unit>

	// offset = 10 * page | type = work/edition/film
	@GET("/bookcasechange{id}")
	fun getBookcase(@Path("id") bookcaseId: Int,
					@Query("offset") offset: Int,
					@Query("type") type: Int): Observable<Unit>

	// shared = on/off | type = work/edition/film
	@GET("/bookcasecreate{type}")
	fun createBookcase(@Path("type") @Query("type") type: String,
					   @Query("name") name: String,
					   @Query("shared") shared: String): Observable<Unit>

	@GET("/bookcaseclick{item_id}bc{bookcase_id}change{to_add}")
	fun editItemAtBookcase(@Path("item_id") itemId: Int,
						   @Path("bookcase_id") bookcaseId: String,
						   @Path("to_add") toAdd: Boolean): Observable<Unit>

	@GET("/bookcasecomm{bookcase_id}item{item_id}comm")
	fun addCommentToBookcaseItem(@Path("bookcase_id") bookcaseId: String,
								 @Path("item_id") itemId: Int,
								 @Query("txt") comment: String): Observable<Unit>

	// action = plus/minus
	@GET("/votepost{id}{action}")
	fun voteForForumPost(@Path("id") postId: Int,
						 @Path("action") action: String): Observable<Unit>

	// genre_id = 1-4 | language_id = 0-2 | year_id = 0-2
	@GET("/recomsbykorrel?bygenre{genre_id}=on")
	fun getRecommendations(@Path("genre_id") genreId: Int,
						   @Query("bylang") languageId: Int,
						   @Query("byyear") yearId: Int): Observable<Unit>

	@GET("/rtrashwork{id}")
	fun removeRecommendation(@Path("id") workId: Int): Observable<Unit>

	// type = blog/topic/forumtopic
	@GET("/edit{type}{id}subscriber")
	fun reverseSubscribe(@Path("type") type: String,
						 @Path("id") blogId: Int): Observable<Unit>
}

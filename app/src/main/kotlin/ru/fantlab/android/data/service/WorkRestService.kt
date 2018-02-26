package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Response

interface WorkRestService {

	@GET("work/{id}/responses")
	fun getResponses(
			@Path(value = "id") authorId: Int,
			@Query(value = "page") page: Int = 1,
			@Query(value = "sort") sort: String = "date"
	): Observable<Pageable<Response>>
}
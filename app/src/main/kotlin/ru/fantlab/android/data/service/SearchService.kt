package ru.fantlab.android.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fantlab.android.data.dao.*

interface SearchService {

	@GET("search-autors")
	fun searchAuthors(
			@Query(value = "q", encoded = true) query: String,
			@Query(value = "page") page: Int
	) : Observable<Pageable<SearchAuthorsModel>>

	@GET("search-works")
	fun searchWorks(
			@Query(value = "q", encoded = true) query: String,
			@Query(value = "page") page: Int
	) : Observable<Pageable<SearchWorksModel>>

	@GET("search-editions")
	fun searchEditions(
			@Query(value = "q", encoded = true) query: String,
			@Query(value = "page") page: Int
	) : Observable<Pageable<SearchEditionsModel>>

	@GET("search-awards")
	fun searchAwards(
			@Query(value = "q", encoded = true) query: String,
			@Query(value = "page") page: Int
	) : Observable<Pageable<SearchAwardsModel>>
}
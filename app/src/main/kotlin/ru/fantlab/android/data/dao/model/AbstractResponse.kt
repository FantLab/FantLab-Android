package ru.fantlab.android.data.dao.model

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import ru.fantlab.android.App
import ru.fantlab.android.helper.single

// todo заменить на реальные поля
@Entity
abstract class AbstractResponse {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	companion object {

		fun save(responses: List<Response>?): Disposable {
			return Single.fromPublisher<Any> { s ->
				try {
					val dataSource = App.dataStore.toBlocking()
					if (responses != null && !responses.isEmpty()) {
						for (response in responses) {
							dataSource.delete(Response::class.java).where(Response.ID.eq(response.id)).get().value()
							dataSource.insert(response)
						}
					}
					s.onNext("")
				} catch (e: Exception) {
					s.onError(e)
				}

				s.onComplete()
			}.single().subscribe(
					{ /*donothing*/ },
					{
						throwable: Throwable -> throwable.printStackTrace()
					}
			)
		}

		fun getResponses(): Single<List<Response>> {
			return App.dataStore
					.select(Response::class.java)
					//.orderBy(Response.PUB_DATE.desc())
					.get()
					.observable()
					.toList()
					.single()
		}
	}
}
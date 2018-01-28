package ru.fantlab.android.data.dao.model

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import ru.fantlab.android.App
import ru.fantlab.android.helper.single
import timber.log.Timber
import java.util.*

// todo заменить на реальные поля
@Entity
abstract class AbstractNews {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	@JvmField
	@Column
	var title: String? = null

	@JvmField
	@Column
	var description: String? = null

	@JvmField
	@Column
	var pubDate: Date? = null

	@JvmField
	@Column
	var author: String? = null

	companion object {

		fun save(news: List<News>?): Disposable {
			return Single.fromPublisher<Any> { s ->
				try {
					val dataSource = App.dataStore.toBlocking()
					if (news != null && !news.isEmpty()) {
						for (newsEntry in news) {
							dataSource.delete(News::class.java).where(News.ID.eq(newsEntry.id)).get().value()
							dataSource.insert(newsEntry)
						}
					}
					s.onNext("")
				} catch (e: Exception) {
					s.onError(e)
				}

				s.onComplete()
			}.single().subscribe({ }, Timber::e)
		}

		fun getNews(): Single<List<News>> {
			return App.dataStore
					.select(News::class.java)
					//.orderBy(News.PUB_DATE.desc())
					.get()
					.observable()
					.toList()
					.single()
		}
	}
}
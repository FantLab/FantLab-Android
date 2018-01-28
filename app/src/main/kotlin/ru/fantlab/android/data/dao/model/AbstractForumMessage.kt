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
abstract class AbstractForumMessage {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	companion object {

		fun save(messages: List<ForumMessage>?): Disposable {
			return Single.fromPublisher<Any> { s ->
				try {
					val dataSource = App.dataStore.toBlocking()
					if (messages != null && !messages.isEmpty()) {
						for (message in messages) {
							dataSource.delete(ForumMessage::class.java).where(ForumMessage.ID.eq(message.id)).get().value()
							dataSource.insert(message)
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

		fun getMessages(): Single<List<ForumMessage>> {
			return App.dataStore
					.select(ForumMessage::class.java)
					//.orderBy(ForumMessage.PUB_DATE.desc())
					.get()
					.observable()
					.toList()
					.single()
		}
	}
}
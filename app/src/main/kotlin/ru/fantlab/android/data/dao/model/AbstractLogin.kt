package ru.fantlab.android.data.dao.model

import io.requery.Column
import io.requery.Entity
import ru.fantlab.android.App

@Entity
abstract class AbstractLogin {

	@JvmField
	@Column
	var login: String? = null

	@JvmField
	@Column
	var token: String? = null

	@JvmField
	@Column
	var isLoggedIn: Boolean? = null

	@JvmField
	@Column
	var name: String? = null

	@JvmField
	@Column
	var avatarUrl: String? = null

	companion object {

		fun getUser(): Login? {
			return App.dataStore
					.select(Login::class.java)
					.where(Login.LOGIN.notNull()
							.and(Login.TOKEN.notNull())
							.and(Login.IS_LOGGED_IN.eq(true)))
					.get()
					.firstOrNull()
		}

		fun logout() {
			val login = getUser() ?: return
			App.dataStore.toBlocking().delete(login)
		}
	}
}
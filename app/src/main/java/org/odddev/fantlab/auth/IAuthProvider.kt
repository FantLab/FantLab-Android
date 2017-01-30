package org.odddev.fantlab.auth

import rx.Single

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

interface IAuthProvider {

	fun login(username: String, password: String): Single<Boolean>

	fun register(username: String, password: String, email: String): Single<Boolean>
}

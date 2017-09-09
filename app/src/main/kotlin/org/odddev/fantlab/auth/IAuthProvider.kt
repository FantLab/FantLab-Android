package org.odddev.fantlab.auth

import io.reactivex.Single

interface IAuthProvider {

	fun login(username: String, password: String): Single<Boolean>
}
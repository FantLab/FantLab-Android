package org.odddev.fantlab.auth

import io.reactivex.Flowable

interface IAuthProvider {

	fun login(username: String, password: String): Flowable<Boolean>
}
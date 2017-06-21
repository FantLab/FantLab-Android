package org.odddev.fantlab.home

import io.reactivex.Observable

interface IHomeProvider {

	fun getUserName(): Observable<String>

	fun clearCookie()
}

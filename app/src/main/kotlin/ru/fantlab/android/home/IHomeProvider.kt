package ru.fantlab.android.home

import io.reactivex.Observable

interface IHomeProvider {

	fun getUserName(): Observable<String>

	fun clearCookie()

	fun clearUserName()
}

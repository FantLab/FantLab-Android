package ru.fantlab.android.old.home

import io.reactivex.Observable

interface IHomeProvider {

	fun getUserName(): Observable<String>

	fun clearCookie()

	fun clearUserName()
}

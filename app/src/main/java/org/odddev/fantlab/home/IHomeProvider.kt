package org.odddev.fantlab.home

import io.reactivex.Observable

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IHomeProvider {

	fun getUserName(): Observable<String>

	fun clearCookie()
}

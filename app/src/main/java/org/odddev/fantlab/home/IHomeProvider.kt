package org.odddev.fantlab.home

import rx.Single

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IHomeProvider {

	fun getUserName(): Single<String>

	fun clearCookie()
}

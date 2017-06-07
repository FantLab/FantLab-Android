package org.odddev.fantlab.autors

import io.reactivex.Observable

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAutorsProvider {

	fun getAutors(): Observable<AutorsResponse>
}

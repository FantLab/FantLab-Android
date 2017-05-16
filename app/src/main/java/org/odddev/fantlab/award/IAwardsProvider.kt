package org.odddev.fantlab.award

import io.reactivex.Observable

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAwardsProvider {

	fun getAwards(): Observable<List<Award>>
}

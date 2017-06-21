package org.odddev.fantlab.award

import io.reactivex.Observable

interface IAwardsProvider {

	fun getAwards(): Observable<List<Award>>
}

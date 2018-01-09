package ru.fantlab.android.award

import io.reactivex.Observable

interface IAwardsProvider {

	fun getAwards(): Observable<List<Award>>
}

package ru.fantlab.android.old.award

import io.reactivex.Observable

interface IAwardsProvider {

	fun getAwards(): Observable<List<Award>>
}

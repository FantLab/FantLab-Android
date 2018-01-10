package ru.fantlab.android.old.edition

import io.reactivex.Flowable

interface IEditionProvider {

	fun getEdition(id: Int): Flowable<Edition>
}

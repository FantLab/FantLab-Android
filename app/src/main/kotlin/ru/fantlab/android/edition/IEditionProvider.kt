package ru.fantlab.android.edition

import io.reactivex.Flowable

interface IEditionProvider {

	fun getEdition(id: Int): Flowable<Edition>
}

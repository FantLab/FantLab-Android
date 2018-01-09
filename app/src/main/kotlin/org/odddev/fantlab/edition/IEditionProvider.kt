package org.odddev.fantlab.edition

import io.reactivex.Flowable

interface IEditionProvider {

	fun getEdition(id: Int): Flowable<Edition>
}

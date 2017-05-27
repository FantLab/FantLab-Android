package org.odddev.fantlab.autors.autor

import io.reactivex.Observable

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAutorProvider {

	fun getAutor(id: Int): Observable<AutorFull>
}

package org.odddev.fantlab.autors.autor

import io.reactivex.Observable

interface IAutorProvider {

	fun getAutor(id: Int): Observable<AutorFull>
}

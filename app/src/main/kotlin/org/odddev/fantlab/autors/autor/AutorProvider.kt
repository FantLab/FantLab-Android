package org.odddev.fantlab.autors.autor

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.autors.AutorsResponse
import org.odddev.fantlab.autors.IAutorsProvider
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

class AutorProvider : IAutorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAutor(id: Int): Observable<AutorFull> =
			serverApi.getAutor(id, 1, 1, 1, 1, 1)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())

}

package org.odddev.fantlab.autors.autor

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AutorProvider : IAutorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAutor(id: Int): Observable<AutorFull> =
			serverApi.getAutor(id)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())

}

package ru.fantlab.android

import android.app.Application
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import ru.fantlab.android.provider.db.DbProvider
import ru.fantlab.android.provider.fabric.FabricProvider
import ru.fantlab.android.provider.stetho.StethoProvider
import ru.fantlab.android.provider.timber.TimberProvider

class App : Application() {

	companion object {
		lateinit var instance: App
		lateinit var dataStore: ReactiveEntityStore<Persistable>
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
		init()
	}

	private fun init() {
		FabricProvider.initFabric(this)
		TimberProvider.setupTimber()
		if (BuildConfig.DEBUG) {
			StethoProvider.initStetho(this)
		}
		dataStore = DbProvider.initDataStore(this, 1)
	}
}

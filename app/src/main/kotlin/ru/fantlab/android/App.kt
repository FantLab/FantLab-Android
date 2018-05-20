package ru.fantlab.android

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import ru.fantlab.android.provider.db.DbProvider
import ru.fantlab.android.provider.fabric.FabricProvider
import ru.fantlab.android.provider.stetho.StethoProvider
import ru.fantlab.android.provider.timber.TimberProvider
import shortbread.Shortbread

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
		Shortbread.create(this)
		FuelManager.instance.apply {
			basePath = "https://api.fantlab.ru/"
			baseHeaders = mapOf("User-Agent" to "FantLab for Android v${BuildConfig.VERSION_NAME}")
		}
	}
}

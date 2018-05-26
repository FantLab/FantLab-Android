package ru.fantlab.android

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.validatorResponseInterceptor
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.fabric.FabricProvider
import ru.fantlab.android.provider.stetho.StethoProvider
import ru.fantlab.android.provider.timber.TimberProvider
import shortbread.Shortbread

class App : Application() {

	companion object {
		lateinit var instance: App
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
		Shortbread.create(this)
		FuelManager.instance.apply {
			// to prevent from auto redirection
			removeAllResponseInterceptors()
			addResponseInterceptor(validatorResponseInterceptor(200..299))
			baseHeaders = mapOf(
					"User-Agent" to "FantLab for Android v${BuildConfig.VERSION_NAME}",
					"Cookie" to (PrefGetter.getToken() ?: "")
			)
		}
	}
}

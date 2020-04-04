package ru.fantlab.android

import android.app.Application
import androidx.preference.PreferenceManager
import com.github.kittinunf.fuel.core.FuelManager
import ru.fantlab.android.data.service.DbResponseInterceptor
import ru.fantlab.android.data.service.AuthenticatorInterceptor
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.fabric.FabricProvider
import ru.fantlab.android.provider.stetho.StethoProvider
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.provider.storage.smiles.SmileManager
import ru.fantlab.android.provider.storage.WorkTypesProvider
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
		PreferenceManager.setDefaultValues(this, R.xml.customization_settings, false);
		//PreferenceManager.setDefaultValues(this, R.xml.forum_settings, false);
		FabricProvider.initFabric(this)
		DbProvider.initDatabase(this)
		TimberProvider.setupTimber()
		if (BuildConfig.DEBUG) {
			StethoProvider.initStetho(this)
		}
		Shortbread.create(this)
		initFuel()
		WorkTypesProvider.init()
		SmileManager.load()
	}

	fun initFuel() {
		FuelManager.instance.apply {
			removeAllResponseInterceptors()
			addResponseInterceptor(AuthenticatorInterceptor)
			addResponseInterceptor(DbResponseInterceptor)
			baseHeaders = mapOf(
					"User-Agent" to "FantLab for Android v${BuildConfig.VERSION_NAME}",
					"X-Session" to (PrefGetter.getToken() ?: "")
			)
		}
	}
}

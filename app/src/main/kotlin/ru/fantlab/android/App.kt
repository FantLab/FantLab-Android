package ru.fantlab.android

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveSupport
import io.requery.sql.EntityDataStore
import io.requery.sql.TableCreationMode
import ru.fantlab.android.data.dao.model.Models

class App : Application() {

	private lateinit var instance: App
	private lateinit var dataStore: ReactiveEntityStore<Persistable>

	override fun onCreate() {
		super.onCreate()
		instance = this
		init()
	}

	private fun init() {
		setupDataStore()
		if (!BuildConfig.DEBUG) Fabric.with(this, Crashlytics())
		//Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else TimberCrashlyticsTree())
		val builder = Stetho.newInitializerBuilder(this)
		builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
		builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
		Stetho.initialize(builder.build())
	}

	private fun setupDataStore() {
		val model = Models.DEFAULT
		val source = DatabaseSource(this, model, "Fantlab-DB", 1)
		val configuration = source.configuration
		if (BuildConfig.DEBUG) {
			source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)
		}
		dataStore = ReactiveSupport.toReactiveStore(EntityDataStore(configuration))
	}
}

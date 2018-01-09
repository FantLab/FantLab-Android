package ru.fantlab.android.core

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.core.di.AppModule
import ru.fantlab.android.core.di.DaggerAppComponent
import ru.fantlab.android.core.di.Injector
import ru.fantlab.android.core.utils.TimberCrashlyticsTree
import timber.log.Timber

class FantlabApplication : Application() {

	override fun onCreate() {
		super.onCreate()

		initCrashlytics()
		initTimber()
		initStetho()
		initDagger()
	}

	private fun initCrashlytics() {
		if (!BuildConfig.DEBUG) Fabric.with(this, Crashlytics())
	}

	private fun initTimber() {
		Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else TimberCrashlyticsTree())
	}

	private fun initStetho() {
		val builder = Stetho.newInitializerBuilder(this)
		builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
		builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
		Stetho.initialize(builder.build())
	}

	private fun initDagger() {
		Injector.init(DaggerAppComponent.builder().appModule(AppModule(this)).build())
	}
}

package org.odddev.fantlab.core

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import org.odddev.fantlab.BuildConfig
import org.odddev.fantlab.core.di.AppModule
import org.odddev.fantlab.core.di.DaggerAppComponent
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.utils.TimberCrashlyticsTree
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
		val initializer = builder.build()
		Stetho.initialize(initializer)
	}

	private fun initDagger() {
		Injector.init(DaggerAppComponent.builder().appModule(AppModule(this)).build())
	}
}

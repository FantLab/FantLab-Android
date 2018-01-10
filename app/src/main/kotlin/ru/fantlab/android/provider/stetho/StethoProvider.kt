package ru.fantlab.android.provider.stetho

import android.content.Context
import com.facebook.stetho.Stetho

object StethoProvider {

	fun initStetho(context: Context) {
		val builder = Stetho.newInitializerBuilder(context)
		builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
		builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
		Stetho.initialize(builder.build())
	}
}
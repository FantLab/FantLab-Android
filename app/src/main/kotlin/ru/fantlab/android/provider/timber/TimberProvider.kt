package ru.fantlab.android.provider.timber

import android.util.Log
import com.crashlytics.android.Crashlytics
import ru.fantlab.android.BuildConfig
import timber.log.Timber

object TimberProvider {

	fun setupTimber() {
		Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())
	}

	internal class CrashlyticsTree : Timber.Tree() {

		override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
			if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) return

			Crashlytics.setInt("priority", priority)
			Crashlytics.setString("tag", tag)
			Crashlytics.setString("message", message)
			Crashlytics.logException(t ?: Exception(message))
		}
	}
}
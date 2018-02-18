package ru.fantlab.android.provider.fabric

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.fantlab.android.BuildConfig

/**
 * Created by kosh on 14/08/2017.
 */
object FabricProvider {

	fun initFabric(context: Context) {
		val fabric = Fabric.Builder(context)
				.kits(Crashlytics.Builder()
						.core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
						.build())
				.debuggable(BuildConfig.DEBUG)
				.build()
		Fabric.with(fabric)
	}
}

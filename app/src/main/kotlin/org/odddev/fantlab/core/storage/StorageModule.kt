package org.odddev.fantlab.core.storage

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author kenrube
 * *
 * @since 29.09.16
 */

@Module
class StorageModule {

	@Provides
	@Singleton
	fun getStorageManager(context: Context) = StorageManager(context)
}

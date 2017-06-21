package org.odddev.fantlab.core.storage

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

	@Provides
	@Singleton
	fun getStorageManager(context: Context) = StorageManager(context)
}

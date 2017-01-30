package org.odddev.fantlab.core.storage

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * @author kenrube
 * *
 * @since 29.09.16
 */

@Module
class StorageModule {

	@Provides
	@Singleton
	fun getStorageManager(context: Context): StorageManager = StorageManager(context)
}

package org.odddev.fantlab.core.network

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

@Module
class NetworkModule {

	@Provides
	@Singleton
	fun provideServerApi(): IServerApi = ServerApiBuilder.createApi()
}

package org.odddev.fantlab.core.network

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

@Module
class NetworkModule {

	@Provides
	@Singleton
	fun provideServerApi() = ServerApiBuilder.createApi()
}

package org.odddev.fantlab.core.di

import android.content.Context

import org.odddev.fantlab.core.network.INetworkChecker
import org.odddev.fantlab.core.network.NetworkChecker

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * @author kenrube
 * *
 * @since 23.08.16
 */

@Module
class AppModule(internal val context: Context) {

	@Singleton
	@Provides
	fun getContext(): Context = context

	@Singleton
	@Provides
	fun provideNetworkChecker(): INetworkChecker = NetworkChecker()
}

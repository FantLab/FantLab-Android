package org.odddev.fantlab.core.di

import android.content.Context

import org.odddev.fantlab.core.network.INetworkChecker
import org.odddev.fantlab.core.network.NetworkChecker
import org.odddev.fantlab.core.rx.ISchedulersResolver
import org.odddev.fantlab.core.rx.SchedulersResolver

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
	fun provideSchedulersResolver(): ISchedulersResolver = SchedulersResolver()

	@Singleton
	@Provides
	fun provideNetworkChecker(): INetworkChecker = NetworkChecker()
}

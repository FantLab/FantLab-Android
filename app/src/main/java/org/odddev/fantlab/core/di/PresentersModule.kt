package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

/**
 * @author kenrube
 * *
 * @since 16.09.16
 */

@Module
class PresentersModule {

	@Provides
	fun provideCompositeSubscription(): CompositeSubscription = CompositeSubscription()
}

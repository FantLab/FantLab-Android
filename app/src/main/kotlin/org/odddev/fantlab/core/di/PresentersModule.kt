package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * @author kenrube
 * *
 * @since 16.09.16
 */

@Module
class PresentersModule {

	@Provides
	fun provideCompositeDisposable() = CompositeDisposable()
}

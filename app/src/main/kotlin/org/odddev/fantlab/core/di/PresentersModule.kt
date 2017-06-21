package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class PresentersModule {

	@Provides
	fun provideCompositeDisposable() = CompositeDisposable()
}

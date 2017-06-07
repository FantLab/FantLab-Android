package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import org.odddev.fantlab.autors.AutorsProvider
import org.odddev.fantlab.autors.IAutorsProvider
import org.odddev.fantlab.autors.autor.AutorProvider
import org.odddev.fantlab.autors.autor.IAutorProvider
import org.odddev.fantlab.award.AwardsProvider
import org.odddev.fantlab.award.IAwardsProvider
import org.odddev.fantlab.home.HomeProvider
import org.odddev.fantlab.home.IHomeProvider

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

@Module
class ProvidersModule {

	@Provides
	fun provideAwardsProvider(): IAwardsProvider = AwardsProvider()

	@Provides
	fun provideHomeProvider(): IHomeProvider = HomeProvider()

	@Provides
	fun provideAutorsProvider(): IAutorsProvider = AutorsProvider()

	@Provides
	fun provideAutorProvider(): IAutorProvider = AutorProvider()
}

package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import org.odddev.fantlab.auth.AuthProvider
import org.odddev.fantlab.auth.IAuthProvider
import org.odddev.fantlab.autors.AutorsProvider
import org.odddev.fantlab.autors.IAutorsProvider
import org.odddev.fantlab.autors.autor.AuthorProvider
import org.odddev.fantlab.autors.autor.IAuthorProvider
import org.odddev.fantlab.award.AwardsProvider
import org.odddev.fantlab.award.IAwardsProvider
import org.odddev.fantlab.home.HomeProvider
import org.odddev.fantlab.home.IHomeProvider

@Module
class ProvidersModule {

	@Provides
	fun providesAuthProvider(): IAuthProvider = AuthProvider()

	@Provides
	fun provideAwardsProvider(): IAwardsProvider = AwardsProvider()

	@Provides
	fun provideHomeProvider(): IHomeProvider = HomeProvider()

	@Provides
	fun provideAuthorsProvider(): IAutorsProvider = AutorsProvider()

	@Provides
	fun provideAuthorProvider(): IAuthorProvider = AuthorProvider()
}

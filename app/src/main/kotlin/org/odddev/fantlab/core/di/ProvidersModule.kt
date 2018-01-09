package org.odddev.fantlab.core.di

import dagger.Module
import dagger.Provides
import org.odddev.fantlab.auth.AuthProvider
import org.odddev.fantlab.auth.IAuthProvider
import org.odddev.fantlab.author.AuthorProvider
import org.odddev.fantlab.author.IAuthorProvider
import org.odddev.fantlab.authors.AuthorsProvider
import org.odddev.fantlab.authors.IAuthorsProvider
import org.odddev.fantlab.award.AwardsProvider
import org.odddev.fantlab.award.IAwardsProvider
import org.odddev.fantlab.edition.EditionProvider
import org.odddev.fantlab.edition.IEditionProvider
import org.odddev.fantlab.home.HomeProvider
import org.odddev.fantlab.home.IHomeProvider
import org.odddev.fantlab.search.ISearchProvider
import org.odddev.fantlab.search.SearchProvider

@Module
class ProvidersModule {

	@Provides
	fun providesAuthProvider(): IAuthProvider = AuthProvider()

	@Provides
	fun provideAwardsProvider(): IAwardsProvider = AwardsProvider()

	@Provides
	fun provideHomeProvider(): IHomeProvider = HomeProvider()

	@Provides
	fun provideAuthorsProvider(): IAuthorsProvider = AuthorsProvider()

	@Provides
	fun provideAuthorProvider(): IAuthorProvider = AuthorProvider()

	@Provides
	fun provideSearchProvider(): ISearchProvider = SearchProvider()

	@Provides
	fun provideEditionProvider(): IEditionProvider = EditionProvider()
}

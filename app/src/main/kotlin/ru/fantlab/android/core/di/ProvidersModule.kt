package ru.fantlab.android.core.di

import dagger.Module
import dagger.Provides
import ru.fantlab.android.auth.AuthProvider
import ru.fantlab.android.auth.IAuthProvider
import ru.fantlab.android.author.AuthorProvider
import ru.fantlab.android.author.IAuthorProvider
import ru.fantlab.android.authors.AuthorsProvider
import ru.fantlab.android.authors.IAuthorsProvider
import ru.fantlab.android.award.AwardsProvider
import ru.fantlab.android.award.IAwardsProvider
import ru.fantlab.android.edition.EditionProvider
import ru.fantlab.android.edition.IEditionProvider
import ru.fantlab.android.home.HomeProvider
import ru.fantlab.android.home.IHomeProvider
import ru.fantlab.android.search.ISearchProvider
import ru.fantlab.android.search.SearchProvider

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

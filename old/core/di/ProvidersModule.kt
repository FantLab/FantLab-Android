package ru.fantlab.android.old.core.di

import dagger.Module
import dagger.Provides
import ru.fantlab.android.old.auth.AuthProvider
import ru.fantlab.android.old.auth.IAuthProvider
import ru.fantlab.android.old.author.AuthorProvider
import ru.fantlab.android.old.author.IAuthorProvider
import ru.fantlab.android.old.authors.AuthorsProvider
import ru.fantlab.android.old.authors.IAuthorsProvider
import ru.fantlab.android.old.award.AwardsProvider
import ru.fantlab.android.old.award.IAwardsProvider
import ru.fantlab.android.old.edition.EditionProvider
import ru.fantlab.android.old.edition.IEditionProvider
import ru.fantlab.android.old.home.HomeProvider
import ru.fantlab.android.old.home.IHomeProvider
import ru.fantlab.android.old.search.ISearchProvider
import ru.fantlab.android.old.search.SearchProvider

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

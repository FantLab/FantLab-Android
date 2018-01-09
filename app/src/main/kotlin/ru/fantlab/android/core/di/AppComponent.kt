package ru.fantlab.android.core.di

import dagger.Component
import ru.fantlab.android.auth.AuthPresenter
import ru.fantlab.android.auth.AuthProvider
import ru.fantlab.android.author.AuthorPresenter
import ru.fantlab.android.author.AuthorProvider
import ru.fantlab.android.authors.AuthorsPresenter
import ru.fantlab.android.authors.AuthorsProvider
import ru.fantlab.android.award.AwardsPresenter
import ru.fantlab.android.award.AwardsProvider
import ru.fantlab.android.core.network.HeaderInterceptor
import ru.fantlab.android.core.network.NetworkChecker
import ru.fantlab.android.core.network.NetworkModule
import ru.fantlab.android.core.storage.StorageModule
import ru.fantlab.android.edition.EditionPresenter
import ru.fantlab.android.edition.EditionProvider
import ru.fantlab.android.home.HomePresenter
import ru.fantlab.android.home.HomeProvider
import ru.fantlab.android.launch.LaunchActivity
import ru.fantlab.android.search.SearchPresenter
import ru.fantlab.android.search.SearchProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [
	(AppModule::class),
	(ProvidersModule::class),
	(PresentersModule::class),
	(NetworkModule::class),
	(StorageModule::class)
])
interface AppComponent {

	fun inject(networkChecker: NetworkChecker)

	fun inject(launchActivity: LaunchActivity)

	fun inject(headerInterceptor: HeaderInterceptor)

	fun inject(awardsPresenter: AwardsPresenter)

	fun inject(awardsProvider: AwardsProvider)

	fun inject(homeProvider: HomeProvider)

	fun inject(homePresenter: HomePresenter)

	fun inject(authorsProvider: AuthorsProvider)

	fun inject(authorsPresenter: AuthorsPresenter)

	fun inject(authorProvider: AuthorProvider)

	fun inject(authorPresenter: AuthorPresenter)

	fun inject(authPresenter: AuthPresenter)

	fun inject(authProvider: AuthProvider)

	fun inject(searchProvider: SearchProvider)

	fun inject(searchPresenter: SearchPresenter)

	fun inject(editionPresenter: EditionPresenter)

	fun inject(editionProvider: EditionProvider)
}

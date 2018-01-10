package ru.fantlab.android.old.core.di

import dagger.Component
import ru.fantlab.android.old.auth.AuthPresenter
import ru.fantlab.android.old.auth.AuthProvider
import ru.fantlab.android.old.author.AuthorPresenter
import ru.fantlab.android.old.author.AuthorProvider
import ru.fantlab.android.old.authors.AuthorsPresenter
import ru.fantlab.android.old.authors.AuthorsProvider
import ru.fantlab.android.old.award.AwardsPresenter
import ru.fantlab.android.old.award.AwardsProvider
import ru.fantlab.android.old.core.network.HeaderInterceptor
import ru.fantlab.android.old.core.network.NetworkChecker
import ru.fantlab.android.old.core.network.NetworkModule
import ru.fantlab.android.old.core.storage.StorageModule
import ru.fantlab.android.old.edition.EditionPresenter
import ru.fantlab.android.old.edition.EditionProvider
import ru.fantlab.android.old.home.HomePresenter
import ru.fantlab.android.old.home.HomeProvider
import ru.fantlab.android.old.launch.LaunchActivity
import ru.fantlab.android.old.search.SearchPresenter
import ru.fantlab.android.old.search.SearchProvider
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

package org.odddev.fantlab.core.di

import dagger.Component
import org.odddev.fantlab.auth.AuthPresenter
import org.odddev.fantlab.auth.AuthProvider
import org.odddev.fantlab.authors.AuthorsPresenter
import org.odddev.fantlab.authors.AuthorsProvider
import org.odddev.fantlab.author.AuthorPresenter
import org.odddev.fantlab.author.AuthorProvider
import org.odddev.fantlab.award.AwardsPresenter
import org.odddev.fantlab.award.AwardsProvider
import org.odddev.fantlab.core.network.HeaderInterceptor
import org.odddev.fantlab.core.network.NetworkChecker
import org.odddev.fantlab.core.network.NetworkModule
import org.odddev.fantlab.core.storage.StorageModule
import org.odddev.fantlab.home.HomePresenter
import org.odddev.fantlab.home.HomeProvider
import org.odddev.fantlab.launch.LaunchActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ProvidersModule::class, PresentersModule::class,
		NetworkModule::class, StorageModule::class))
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
}

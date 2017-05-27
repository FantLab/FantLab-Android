package org.odddev.fantlab.core.di

import dagger.Component
import org.odddev.fantlab.autors.AutorsPresenter
import org.odddev.fantlab.autors.AutorsProvider
import org.odddev.fantlab.autors.autor.AutorPresenter
import org.odddev.fantlab.autors.autor.AutorProvider
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

/**
 * @author kenrube
 * *
 * @since 23.08.16
 */

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

	fun inject(autorsProvider: AutorsProvider)

	fun inject(autorsPresenter: AutorsPresenter)

	fun inject(autorProvider: AutorProvider)

	fun inject(autorPresenter: AutorPresenter)
}

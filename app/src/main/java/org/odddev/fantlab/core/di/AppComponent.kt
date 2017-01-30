package org.odddev.fantlab.core.di

import org.odddev.fantlab.auth.AuthProvider
import org.odddev.fantlab.auth.login.LoginPresenter
import org.odddev.fantlab.auth.reg.RegPresenter
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

import dagger.Component

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

	fun inject(loginPresenter: LoginPresenter)

	fun inject(userProvider: AuthProvider)

	fun inject(regPresenter: RegPresenter)

	fun inject(launchActivity: LaunchActivity)

	fun inject(headerInterceptor: HeaderInterceptor)

	fun inject(awardsPresenter: AwardsPresenter)

	fun inject(awardsProvider: AwardsProvider)

	fun inject(homeProvider: HomeProvider)

	fun inject(homePresenter: HomePresenter)
}

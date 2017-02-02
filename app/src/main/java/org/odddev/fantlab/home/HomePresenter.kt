package org.odddev.fantlab.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.odddev.fantlab.core.di.Injector
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

@InjectViewState
class HomePresenter : MvpPresenter<IHomeView>() {

	@Inject
	lateinit var provider: IHomeProvider

	@Inject
	lateinit var compositeSubscription: CompositeSubscription

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getUserName() {
		compositeSubscription.add(provider.getUserName().subscribe({ showUserName(it) }))
	}

	internal fun clearCookie() {
		provider.clearCookie()
	}

	private fun showUserName(userName: String) {
		viewState.showUserName(userName)
	}

	override fun onDestroy() {
		compositeSubscription.unsubscribe()
		super.onDestroy()
	}
}

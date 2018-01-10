package ru.fantlab.android.old.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.fantlab.android.old.core.di.Injector
import javax.inject.Inject

@InjectViewState
class HomePresenter : MvpPresenter<IHomeView>() {

	@Inject
	lateinit var provider: IHomeProvider

	@Inject
	lateinit var disposables: CompositeDisposable

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getUserName() {
		disposables.add(provider.getUserName().subscribe({ viewState.showUserName(it) }))
	}

	internal fun clearCookie() {
		provider.clearCookie()
	}

	internal fun clearUserName() {
		provider.clearUserName()
	}

	override fun onDestroy() {
		disposables.clear()
	}
}

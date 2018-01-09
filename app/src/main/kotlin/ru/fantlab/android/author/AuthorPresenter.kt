package ru.fantlab.android.author

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.fantlab.android.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthorPresenter : MvpPresenter<IAuthorView>() {

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var authorProvider: IAuthorProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAuthor(id: Int?) {
		disposables.add(authorProvider
				.getAuthor(id!!)
				.subscribe(
						{
							author -> viewState.showError("Ready")
						},
						{ error ->
							run {
								Timber.e(error)
								viewState.showError(error.message ?: "Error")
							}
						}))
	}

	override fun onDestroy() {
		disposables.clear()
	}
}

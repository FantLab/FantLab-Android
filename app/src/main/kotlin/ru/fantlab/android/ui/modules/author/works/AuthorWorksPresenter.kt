package ru.fantlab.android.ui.modules.author.works

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AuthorWorksPresenter : BasePresenter<AuthorWorksMvp.View>(),
		AuthorWorksMvp.Presenter {

	@com.evernote.android.state.State
	var authorId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or AuthorId is null")
		}
		authorId = bundle.getInt(BundleConstant.EXTRA)
		authorId?.let {
			makeRestCall(
					DataManager.getAuthor(it, showBiblioBlocks = true)
							.map { it.get() }
							.toObservable(),
					Consumer { authorResponse ->
						sendToView { it.onInitViews(
								authorResponse.cycles,
								authorResponse.works!!
						) }
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		authorId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}
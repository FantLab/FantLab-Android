package ru.fantlab.android.ui.modules.author.bibliography

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.author.works.AuthorWorksFragment

class AuthorBibliographyPresenter : BasePresenter<AuthorBibliographyMvp.View>(),
		AuthorBibliographyMvp.Presenter {

	@com.evernote.android.state.State
	var authorId: Int? = null
	private var bibliography: WorksBlocks? = null

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
								authorResponse.cycles
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

	override fun getBibliography(): WorksBlocks? = bibliography

	fun onCallApi() {
		authorId?.let {
			makeRestCall(
					DataManager.getAuthor(it, showBiblioBlocks = true)
							.map { it.get() }
							.toObservable(),
					Consumer { authorResponse ->
						sendToView { it.onInitViews(
								authorResponse.cycles
						) }
					}
			)
		}
	}


}
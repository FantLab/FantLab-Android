package ru.fantlab.android.ui.modules.author.works

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.*

class AuthorWorksPresenter : BasePresenter<AuthorWorksMvp.View>(),
		AuthorWorksMvp.Presenter{

    @com.evernote.android.state.State
	var authorId: Int? = null

	private var works: ArrayList<WorksBlocks.Work> = ArrayList()

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

	override fun getWorks(): ArrayList<WorksBlocks.Work> = works

    override fun onItemClick(position: Int, v: View?, item: WorksBlocks.Work) {
        view?.onItemClicked(item)
    }

    override fun onItemLongClick(position: Int, v: View?, item: WorksBlocks.Work?) {
    }

    fun onCallApi() {
        authorId?.let {
            makeRestCall(
                    DataManager.getAuthor(it, true)
                            .map { it.get() }
                            .toObservable(),
                    Consumer { workResponse ->
                        sendToView { it.onNotifyAdapter() }
                    }
            )}
    }

}
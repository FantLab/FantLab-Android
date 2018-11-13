package ru.fantlab.android.ui.modules.editor.smiles

import io.reactivex.Observable
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.provider.markdown.extension.smiles.SmileManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class SmilePresenter : BasePresenter<SmileMvp.View>(), SmileMvp.Presenter {

	override fun onLoadSmile() {
		manageObservable(Observable.create<Smile> { e ->
			val smiles = SmileManager.getAll()
			smiles?.forEach {
				if (!e.isDisposed) {
					e.onNext(it)
				}
			}
			e.onComplete()
		}.doOnSubscribe { sendToView { it.clearAdapter() } }
				.doOnNext { smile -> sendToView { it.onAddSmile(smile) } })
	}
}
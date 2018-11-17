package ru.fantlab.android.ui.modules.award.overview

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardOverviewPresenter : BasePresenter<AwardOverviewMvp.View>(),
		AwardOverviewMvp.Presenter {

	@com.evernote.android.state.State
	var awardId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or AuthorId is null")
		}
		awardId = bundle.getInt(BundleConstant.EXTRA)
		awardId?.let { it ->
			makeRestCall(
					DataManager.getAward(it, false, false)
							.toObservable(),
					Consumer { awardResponse ->
						sendToView { it.onInitViews(awardResponse.award) }
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		awardId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}
}
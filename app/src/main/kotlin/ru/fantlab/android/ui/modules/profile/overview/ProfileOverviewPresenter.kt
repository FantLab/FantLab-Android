package ru.fantlab.android.ui.modules.profile.overview

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.data.dao.model.getUser
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.RestProvider
import ru.fantlab.android.provider.rest.interceptors.PaginationInterceptor
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileOverviewPresenter : BasePresenter<ProfileOverviewMvp.View>(), ProfileOverviewMvp.Presenter {

	@com.evernote.android.state.State
	var isSuccessResponse: Boolean = false

	@com.evernote.android.state.State
	var userId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or User is null")
		}
		userId = bundle.getInt(BundleConstant.EXTRA)
		userId?.let {
			makeRestCall(RestProvider.getUserService().getUser(it), Consumer { user ->
				user?.save()
				// todo грязный хак, поскольку у нас нет информации о количестве отзывов в запросе на их список
				PaginationInterceptor.totalResponsesCount = user?.responseCount!!
				onSendUserToView(user)
			})
		}
	}

	override fun onError(throwable: Throwable) {
		userId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		val user = getUser(id) ?: return
		onSendUserToView(user)
	}

	override fun onSendUserToView(user: User?) {
		sendToView { view -> view.onInitViews(user) }
	}
}
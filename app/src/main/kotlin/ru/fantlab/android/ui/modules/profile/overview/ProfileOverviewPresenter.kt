package ru.fantlab.android.ui.modules.profile.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.data.dao.response.UserResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getUserPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileOverviewPresenter : BasePresenter<ProfileOverviewMvp.View>(), ProfileOverviewMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		val userId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getUserInternal(userId).toObservable(),
				Consumer { user -> sendToView { it.onInitViews(user) } }
		)
	}

	private fun getUserInternal(userId: Int) =
			getUserFromServer(userId)
					.onErrorResumeNext {
						getUserFromDb(userId)
					}

	private fun getUserFromServer(userId: Int): Single<User> =
			DataManager.getUser(userId)
					.map { getUser(it) }

	private fun getUserFromDb(userId: Int): Single<User> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getUserPath(userId))
					.map { it.response }
					.map { UserResponse.Deserializer().deserialize(it) }
					.map { getUser(it) }

	private fun getUser(response: UserResponse): User = response.user
}
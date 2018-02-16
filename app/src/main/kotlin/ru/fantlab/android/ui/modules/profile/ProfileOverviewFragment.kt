package ru.fantlab.android.ui.modules.profile

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView

class ProfileOverviewFragment : BaseFragment<ProfileOverviewMvp.View, ProfileOverviewPresenter>(),
		ProfileOverviewMvp.View {

	@BindView(R.id.login)
	lateinit var login: FontTextView

	@BindView(R.id.fio)
	lateinit var fio: FontTextView

	@BindView(R.id.sign)
	lateinit var sign: FontTextView

	@BindView(R.id.avatarLayout)
	lateinit var avatarLayout: AvatarLayout

	@BindView(R.id.birthDay)
	lateinit var birthDay: FontTextView

	@BindView(R.id.location)
	lateinit var location: FontTextView

	@BindView(R.id.regDate)
	lateinit var regDate: FontTextView

	@BindView(R.id.lastActionDate)
	lateinit var lastActionDate: FontTextView

	@BindView(R.id.progress)
	lateinit var progress: View

	var user: User? = null

	override fun fragmentLayout(): Int = R.layout.profile_overview_layout

	override fun providePresenter(): ProfileOverviewPresenter = ProfileOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			user = savedInstanceState.getParcelable("user")
			if (user != null) {
				onInitViews(user!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("user", user)
	}

	override fun onInitViews(user: User?) {
		progress.visibility = GONE
		if (user == null) return
		this.user = user
		val gender = if (user.sex == "m") /* male */ "\u2642" else /* female */ "\u2640"
		fio.text = if (user.fio.isNullOrEmpty()) {
			gender
		} else {
			StringBuilder()
					.append(user.fio)
					.append(" (")
					.append(gender)
					.append(")")
		}
		login.text = user.login
		if (user.sign.isNullOrEmpty()) {
			sign.visibility = View.GONE
		} else {
			sign.text = user.sign
		}
		avatarLayout.setUrl("https://${user.avatar}")
		birthDay.text = user.birthDay.getTimeAgo()
		if (user.countryName != null && user.cityName != null) {
			location.text = StringBuilder()
					.append(user.countryName)
					.append(", ")
					.append(user.cityName)
		} else if (user.location != null) {
			location.text = user.location
		} else {
			location.visibility = View.GONE
		}
		regDate.text = user.dateOfReg.getTimeAgo()
		lastActionDate.text = user.dateOfLastAction.getTimeAgo()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = VISIBLE
	}

	override fun hideProgress() {
		progress.visibility = GONE
	}

	override fun showErrorMessage(msgRes: String) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(userId: Int): ProfileOverviewFragment {
			val view = ProfileOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
			return view
		}
	}
}
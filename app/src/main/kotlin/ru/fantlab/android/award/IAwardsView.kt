package ru.fantlab.android.award

import com.arellomobile.mvp.MvpView

interface IAwardsView : MvpView {

	fun showAwards(awards: List<Award>)

	fun showError(message: String)
}

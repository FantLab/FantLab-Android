package ru.fantlab.android.old.award

import com.arellomobile.mvp.MvpView

interface IAwardsView : MvpView {

	fun showAwards(awards: List<Award>)

	fun showError(message: String)
}

package org.odddev.fantlab.award

import com.arellomobile.mvp.MvpView

interface IAwardsView : MvpView {

	fun showAwards(awards: List<Award>)

	fun showError(message: String)
}

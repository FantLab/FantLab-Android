package org.odddev.fantlab.award

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IAwardsView : MvpView {

	fun showAwards(awards: List<Award>)

	fun showError(message: String)
}

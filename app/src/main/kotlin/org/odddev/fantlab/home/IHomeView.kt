package org.odddev.fantlab.home

import com.arellomobile.mvp.MvpView

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

interface IHomeView : MvpView {

	fun showUserName(userName: String)
}

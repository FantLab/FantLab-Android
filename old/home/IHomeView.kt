package ru.fantlab.android.old.home

import com.arellomobile.mvp.MvpView

interface IHomeView : MvpView {

	fun showUserName(userName: String)
}

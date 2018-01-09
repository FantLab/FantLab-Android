package ru.fantlab.android.home

import com.arellomobile.mvp.MvpView

interface IHomeView : MvpView {

	fun showUserName(userName: String)
}

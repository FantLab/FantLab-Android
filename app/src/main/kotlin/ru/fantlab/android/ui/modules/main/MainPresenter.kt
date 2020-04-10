package ru.fantlab.android.ui.modules.main

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.AppHelper.getFragmentByTag
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.main.news.NewsFragment
import ru.fantlab.android.ui.modules.main.responses.ResponsesFragment
import ru.fantlab.android.ui.modules.plans.pubnews.PubnewsFragment

class MainPresenter : BasePresenter<MainMvp.View>(), MainMvp.Presenter {

	override fun onModuleChanged(fragmentManager: FragmentManager, type: MainMvp.NavigationType) {
		val currentVisible = ActivityHelper.getVisibleFragment(fragmentManager)
		val homeView = getFragmentByTag(fragmentManager, NewsFragment.TAG) as NewsFragment?
		val pubnewsView = getFragmentByTag(fragmentManager, PubnewsFragment.TAG) as? PubnewsFragment
		val responsesView = getFragmentByTag(fragmentManager, ResponsesFragment.TAG) as? ResponsesFragment
		when (type) {
			MainMvp.NavigationType.NEWS -> {
				if (homeView == null) {
					AppHelper.onAddAndHide(fragmentManager, NewsFragment(), currentVisible)
				} else {
					AppHelper.onShowHideFragment(fragmentManager, homeView, currentVisible)
				}
			}
			MainMvp.NavigationType.NEWFICTION -> {
				if (pubnewsView == null) {
					AppHelper.onAddAndHide(fragmentManager, PubnewsFragment(), currentVisible)
				} else {
					AppHelper.onShowHideFragment(fragmentManager, pubnewsView, currentVisible)
				}
			}
			MainMvp.NavigationType.RESPONSES -> {
				if (responsesView == null) {
					AppHelper.onAddAndHide(fragmentManager, ResponsesFragment(), currentVisible)
				} else {
					AppHelper.onShowHideFragment(fragmentManager, responsesView, currentVisible)
				}
			}
		}
	}

	override fun onMenuItemSelect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { it.onNavigationChanged(MainMvp.NavigationType.values()[position]) }
	}

	override fun onMenuItemReselect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { it.onScrollTop(position) }
	}
}
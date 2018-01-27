package ru.fantlab.android.ui.modules.main

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.AppHelper.getFragmentByTag
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.main.news.NewsFragment

class MainPresenter : BasePresenter<MainMvp.View>(), MainMvp.Presenter {

	override fun onModuleChanged(fragmentManager: FragmentManager, type: MainMvp.NavigationType) {
		val currentVisible = ActivityHelper.getVisibleFragment(fragmentManager)
		val homeView = getFragmentByTag(fragmentManager, NewsFragment.TAG) as NewsFragment?
		//val responsesView = getFragmentByTag(fragmentManager, ResponsesFeedFragment.TAG) as ResponsesFeedFragment
		//val forumView = getFragmentByTag(fragmentManager, ForumFeedFragment.TAG) as ForumFeedFragment
		when (type) {
			MainMvp.NavigationType.NEWS -> {
				if (homeView == null) {
					onAddAndHide(fragmentManager, NewsFragment(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, homeView, currentVisible)
				}
			}
			/*MainMvp.NavigationType.RESPONSES -> {
				if (responsesView == null) {
					onAddAndHide(fragmentManager, ResponsesFeedFragment.newInstance(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, responsesView, currentVisible)
				}
			}
			MainMvp.NavigationType.FORUM -> {
				if (forumView == null) {
					onAddAndHide(fragmentManager, ForumFeedFragment.newInstance(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, forumView, currentVisible)
				}
			}*/
		}
	}

	private fun onShowHideFragment(fragmentManager: FragmentManager, toShow: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit()
		toShow.onHiddenChanged(false)
	}

	private fun onAddAndHide(fragmentManager: FragmentManager, toAdd: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.hide(toHide)
				.add(R.id.container, toAdd, toAdd.javaClass.simpleName)
				.commit()
		toAdd.onHiddenChanged(false)
	}

	override fun onMenuItemSelect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		view?.onNavigationChanged(MainMvp.NavigationType.values()[position])
	}

	override fun onMenuItemReselect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { view -> view.onScrollTop(position) }
	}
}
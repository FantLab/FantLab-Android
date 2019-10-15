package ru.fantlab.android.ui.modules.main

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
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
					onAddAndHide(fragmentManager, NewsFragment(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, homeView, currentVisible)
				}
			}
			MainMvp.NavigationType.NEWFICTION -> {
				if (pubnewsView == null) {
					onAddAndHide(fragmentManager, PubnewsFragment(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, pubnewsView, currentVisible)
				}
			}
			MainMvp.NavigationType.RESPONSES -> {
				if (responsesView == null) {
					onAddAndHide(fragmentManager, ResponsesFragment(), currentVisible)
				} else {
					onShowHideFragment(fragmentManager, responsesView, currentVisible)
				}
			}
		}
	}

	private fun onShowHideFragment(fragmentManager: FragmentManager, toShow: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.apply { if (toHide != null) hide(toHide) }
				.show(toShow)
				.commit()
		toShow.onHiddenChanged(false)
	}

	private fun onAddAndHide(fragmentManager: FragmentManager, toAdd: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.apply { if (toHide != null) hide(toHide) }
				.add(R.id.container, toAdd, toAdd.javaClass.simpleName)
				.commit()
		toAdd.onHiddenChanged(false)
	}

	override fun onMenuItemSelect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { it.onNavigationChanged(MainMvp.NavigationType.values()[position]) }
	}

	override fun onMenuItemReselect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { it.onScrollTop(position) }
	}
}
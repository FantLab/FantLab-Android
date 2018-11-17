package ru.fantlab.android.ui.modules.main

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.AppHelper.getFragmentByTag
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.main.responses.ResponsesFragment

class MainPresenter : BasePresenter<MainMvp.View>(), MainMvp.Presenter {

	override fun onModuleChanged(fragmentManager: FragmentManager, type: MainMvp.NavigationType) {
		val currentVisible = ActivityHelper.getVisibleFragment(fragmentManager)
		val responsesView = getFragmentByTag(fragmentManager, ResponsesFragment.TAG) as? ResponsesFragment
		when (type) {
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
		sendToView { it.onNavigationChanged(MainMvp.NavigationType.values()[position]) }
	}

	override fun onMenuItemReselect(@IdRes id: Int, position: Int, fromUser: Boolean) {
		sendToView { it.onScrollTop(position) }
	}
}
package ru.fantlab.android.ui.modules.forums

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.forums.forum.ForumFragment
import ru.fantlab.android.ui.modules.forums.topic.TopicFragment


class ForumsPresenter : BasePresenter<ForumsMvp.View>(), ForumsMvp.Presenter {

	override fun openForum(fragmentManager: FragmentManager, page: String, id: Int, title: String, forumId: Int) {
		lateinit var forumFragment: BaseFragment<*, *>
		val arguments = Bundle()

		val currentVisible = ActivityHelper.getVisibleFragment(fragmentManager)
		val forumView = AppHelper.getFragmentByTag(fragmentManager, ForumFragment.TAG) as ForumFragment?
		val topicView = AppHelper.getFragmentByTag(fragmentManager, TopicFragment.TAG) as TopicFragment?

		when (page) {
			ForumFragment.TAG -> {
				forumFragment = ForumFragment()
				arguments.putInt(BundleConstant.EXTRA, id)
				arguments.putString(BundleConstant.EXTRA_TWO, title)
				forumFragment.arguments = arguments

				if (forumView == null) {
					onAddAndHide(fragmentManager, forumFragment, currentVisible)
				} else {
					onShowHideFragment(fragmentManager, forumView, currentVisible)
				}
			}
			TopicFragment.TAG -> {
				if (topicView == null) {
					forumFragment = TopicFragment()
					arguments.putInt(BundleConstant.EXTRA, id)
					arguments.putString(BundleConstant.EXTRA_TWO, title)
					arguments.putInt(BundleConstant.EXTRA_THREE, forumId)
					forumFragment.arguments = arguments

					onAddAndHide(fragmentManager, forumFragment, currentVisible)
				} else {
					onShowHideFragment(fragmentManager, topicView, currentVisible)
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
				.addToBackStack(toAdd.javaClass.simpleName)
				.commit()
		toAdd.onHiddenChanged(false)
	}


}
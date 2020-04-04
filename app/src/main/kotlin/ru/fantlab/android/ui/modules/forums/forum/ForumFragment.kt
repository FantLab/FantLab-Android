package ru.fantlab.android.ui.modules.forums.forum

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ForumTopicsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.forums.ForumsMvp
import ru.fantlab.android.ui.modules.forums.topic.TopicFragment
import java.util.*

class ForumFragment : BaseFragment<ForumMvp.View, ForumPresenter>(),
		ForumMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = ForumPresenter()

	@State
	var forumId = -1
	@State
	var forumTitle = ""

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, forumId) }

	private val adapter: ForumTopicsAdapter by lazy { ForumTopicsAdapter(arrayListOf(), PrefGetter.isForumExtended()) }

	private var forumsCallback: ForumsMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (arguments != null) {
			if (savedInstanceState == null) {
				stateLayout.hideProgress()
			}
			stateLayout.setEmptyText(R.string.no_results)
			stateLayout.setOnReloadListener(this)
			refresh.setOnRefreshListener(this)
			recycler.setEmptyView(stateLayout, refresh)
			adapter.listener = presenter
			recycler.adapter = adapter
			forumId = arguments!!.getInt(BundleConstant.EXTRA)
			forumTitle = arguments!!.getString(BundleConstant.EXTRA_TWO) ?: getString(R.string.forum)
			forumsCallback?.setTitle(forumTitle)
			getLoadMore().initialize(presenter.getCurrentPage(), presenter.getPreviousTotal())
			recycler.addOnScrollListener(getLoadMore())
			presenter.onCallApi(1, forumId)
			fastScroller.attachRecyclerView(recycler)
		}
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (!hidden) forumsCallback?.setTitle(forumTitle)
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}

	override fun onNotifyAdapter(items: ArrayList<Forum.Topic>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			stateLayout.showEmptyState()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		presenter.onCallApi(1, forumId)
	}

	override fun onItemClicked(item: Forum.Topic) {
		forumsCallback?.openForum(TopicFragment.TAG, item.id, item.title, forumId, item.isClosed ?: false)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Forum.Topic) {
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is ForumsMvp.View) {
			forumsCallback = context
		}
	}

	override fun onDetach() {
		forumsCallback = null
		super.onDetach()
	}

	companion object {
		@Keep
		val TAG: String = ForumFragment::class.java.simpleName

		fun newInstance(forumId: Int, forumTitle: String): ForumFragment {
			val view = ForumFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, forumId)
					.put(BundleConstant.EXTRA_TWO, forumTitle)
					.end()
			return view
		}
	}

}
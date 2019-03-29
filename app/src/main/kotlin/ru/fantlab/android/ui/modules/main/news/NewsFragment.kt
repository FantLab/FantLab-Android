package ru.fantlab.android.ui.modules.main.news

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import android.view.View
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.NewsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.main.news.overview.NewsOverviewActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class NewsFragment : BaseFragment<NewsMvp.View, NewsPresenter>(), NewsMvp.View {

	private val adapter: NewsAdapter by lazy { NewsAdapter(arrayListOf()) }

	private val onLoadMore: OnLoadMore<String> by lazy { OnLoadMore(presenter) }

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): NewsPresenter = NewsPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.setEmptyText(R.string.no_news)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		getLoadMore().initialize(presenter.getCurrentPage(), presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		fastScroller.attachRecyclerView(recycler)
		presenter.onFragmentCreated()
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}


	override fun onNotifyAdapter(items: ArrayList<News>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		presenter.onCallApi(1)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		showReload()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showReload()
		super.showMessage(titleRes, msgRes)
	}

	private fun showReload() {
		hideProgress()
		stateLayout.showReload(adapter.itemCount)
	}

	override fun onScrollTop(index: Int) {
		super.onScrollTop(index)
		recycler.scrollToPosition(0)
	}

	override fun onOpenContextMenu(news: News) {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForProfile(recycler.context), news, 0)
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onItemClicked(item: News, view: View?) {
		NewsOverviewActivity.startActivity(context, item)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: News) {
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		if (listItem is News) {
			val authorId = "user(\\d+)\\D".toRegex().find(listItem.author)?.groupValues?.get(1)
			when (item.id) {
				"profile" -> {
					UserPagerActivity.startActivity(context!!, listItem.author.replace("<.*>(.*?)<.*>".toRegex(), "$1"), authorId?.toInt() ?: -1, 0)
				}
				"message" -> {
					startActivity(Intent(activity, EditorActivity::class.java)
							.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
							.putExtra(BundleConstant.ID, authorId)
					)
				}
			}
		}
	}

	override fun onStart() {
		if (presenter != null) adapter.setOnContextMenuListener(this)
		super.onStart()
	}

	@Keep
	companion object {
		val TAG: String = NewsFragment::class.java.simpleName
	}

}
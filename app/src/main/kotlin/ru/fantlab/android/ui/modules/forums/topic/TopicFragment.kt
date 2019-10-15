package ru.fantlab.android.ui.modules.forums.topic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.model.ForumTopic
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.TopicMessagesSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.TopicMessagesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.forums.ForumsMvp
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.layoutManager.LinearManager
import java.util.*


class TopicFragment : BaseFragment<TopicMvp.View, TopicPresenter>(),
		TopicMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = TopicPresenter()

	@State
	var topicId = -1
	@State
	var forumId = -1
	@State
	var topicTitle = ""
	//@State var replyText = ""

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, topicId) }

	private val adapter: TopicMessagesAdapter by lazy { TopicMessagesAdapter(arrayListOf()) }

	private var forumsCallback: ForumsMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (arguments != null) {
			if (savedInstanceState == null) {
				stateLayout.hideProgress()
			}

			if (PrefGetter.getTopicMessagesOrder() == TopicMessagesSortOption.BY_DESCENDING.value) {
				recycler.apply {
					layoutManager = LinearManager(context).apply {
						reverseLayout = true
					}
				}
			}

			stateLayout.setEmptyText(R.string.no_results)
			stateLayout.setOnReloadListener(this)
			refresh.setOnRefreshListener(this)
			recycler.setEmptyView(stateLayout, refresh)
			adapter.listener = presenter
			adapter.setOnContextMenuListener(this)
			recycler.adapter = adapter

			topicId = arguments!!.getInt(BundleConstant.EXTRA)
			topicTitle = arguments!!.getString(BundleConstant.EXTRA_TWO)
					?: getString(R.string.topic)
			forumId = arguments!!.getInt(BundleConstant.EXTRA_THREE)
			forumsCallback?.setTitle(topicTitle)

			getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
			recycler.addOnScrollListener(getLoadMore())
			presenter.onCallApi(1, topicId)

			setHasOptionsMenu(true)
		}
	}

	override fun onNotifyAdapter(items: ArrayList<ForumTopic.Message>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
			fastScroller.attachRecyclerView(recycler)
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

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.topic_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {

			}
			R.id.message -> {
				onShowEditor("")
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		hideProgress()
	}

	override fun onItemClicked(item: ForumTopic.Message) {

	}

	override fun onOpenContextMenu(topicMessage: Forum.Topic) {

	}


	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		if (listItem is ForumTopic.Message) when (item.id) {
			"copy_link" -> {
				val link = "https://fantlab.ru/forum/forum${forumId}page0/topic${topicId}page${presenter.getCurrentPage()}#msg${listItem.id}"
				ActivityHelper.copyToClipboard(context!!, link)
			}
			"reply" -> {
				val replyText = "[b]${listItem.creation.user.login}[/b], "
				onShowEditor(replyText)
			}
			"profile" -> {
				UserPagerActivity.startActivity(context!!, listItem.creation.user.login, listItem.creation.user.id, 0)
			}
		}
	}

	override fun onShowEditor(quoteText: String) {
		if (!isLoggedIn()) {
			showErrorMessage(getString(R.string.unauthorized_user))
			return
		}
		startActivityForResult(Intent(activity, EditorActivity::class.java)
				.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_TOPIC_MESSAGE)
				.putExtra(BundleConstant.EXTRA, quoteText)
				.putExtra(BundleConstant.ID, topicId),
				BundleConstant.NEW_TOPIC_MESSAGE_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && requestCode == BundleConstant.NEW_TOPIC_MESSAGE_CODE) {
			val myMessage = data?.extras?.getParcelable<ForumTopic.Message>(BundleConstant.ITEM)
			recycler.scrollToPosition(0)
			adapter.addItem(myMessage!!, 0)
		}
	}

	override fun onItemLongClicked(position: Int, v: View?, item: ForumTopic.Message) {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForTopicMessage(recycler.context, isLoggedIn()), item, 0)
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
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
		val TAG: String = TopicFragment::class.java.simpleName

		fun newInstance(topicId: Int, topicTitle: String, forumId: Int): TopicFragment {
			val view = TopicFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, topicId)
					.put(BundleConstant.EXTRA_TWO, topicTitle)
					.put(BundleConstant.EXTRA_THREE, forumId)
					.end()
			return view
		}
	}

}
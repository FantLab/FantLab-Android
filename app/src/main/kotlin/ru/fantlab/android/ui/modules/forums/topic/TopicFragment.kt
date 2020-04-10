package ru.fantlab.android.ui.modules.forums.topic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import ru.fantlab.android.data.dao.model.TopicMessage
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.TopicMessagesSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.TopicMessagesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.forums.ForumsMvp
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.layoutManager.LinearManager
import kotlin.collections.ArrayList


class TopicFragment : BaseFragment<TopicMvp.View, TopicPresenter>(),
		TopicMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = TopicPresenter()

	@State
	var topicId = -1
	@State
	var isClosed = false
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

			if (PrefGetter.getTopicMessagesOrder() == TopicMessagesSortOption.BY_NEW.value) {
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
			isClosed = arguments!!.getBoolean(BundleConstant.YES_NO_EXTRA)
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

	override fun onAddToAdapter(items: ArrayList<ForumTopic.Message>, isNewMessage: Boolean) {
		hideProgress()
		if (isNewMessage) adapter.data.removeAt(0)
		adapter.addItems(items, 0)
	}

	override fun onSetPinnedMessage(message: ForumTopic.PinnedMessage?) {
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
		menu.getItem(0).isVisible = isLoggedIn() && !isClosed
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				ActivityHelper.shareUrl(context!!, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendEncodedPath("forum/forum${forumId}page0/topic${topicId}page${presenter.getCurrentPage()}")
						.toString())
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
		showTopicMenu(item)
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
				UserPagerActivity.startActivity(context!!, listItem.creation.user.login, listItem.creation.user.id.toInt(), 0)
			}
			"delete" -> {
				presenter.onDeleteMessage(listItem.id.toInt())
			}
			"edit" -> {
				onShowEditor(listItem.text, true, listItem.id.toInt())
			}
		}
	}

	override fun onShowEditor(quoteText: String, isEditor: Boolean, messageId: Int) {
		if (!isLoggedIn()) {
			showErrorMessage(getString(R.string.unauthorized_user))
			return
		}
		if (isEditor) {
			startActivityForResult(Intent(activity, EditorActivity::class.java)
					.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_EDIT_TOPIC_MESSAGE)
					.putExtra(BundleConstant.EXTRA, quoteText)
					.putExtra(BundleConstant.ID, messageId),
					BundleConstant.EDIT_TOPIC_MESSAGE_CODE)
		} else {
			startActivityForResult(Intent(activity, EditorActivity::class.java)
					.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_TOPIC_MESSAGE)
					.putExtra(BundleConstant.EXTRA, quoteText)
					.putExtra(BundleConstant.ID, topicId),
					BundleConstant.NEW_TOPIC_MESSAGE_CODE)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK) {
			when (requestCode) {
				BundleConstant.NEW_TOPIC_MESSAGE_CODE -> {
					val myMessage = data?.extras?.getParcelable<TopicMessage>(BundleConstant.ITEM)
					if (myMessage != null) {
						recycler.scrollToPosition(0)
						adapter.addItem(myMessage.message, 0)
						presenter.refreshMessages(adapter.data.first().id, true)
					}
				}
				BundleConstant.EDIT_TOPIC_MESSAGE_CODE -> {
					val extra = data?.extras
					if (data != null) {
						val messageId = extra?.getInt(BundleConstant.ID)
						val messageText = extra?.getString(BundleConstant.EXTRA)
						val message = adapter.data.find { it.id.toInt() == messageId }
						val position = adapter.data.indexOf(message)
						if (message != null) {
							adapter.getItem(position).text = messageText!!
							adapter.notifyItemChanged(position)
							presenter.refreshMessages(adapter.data.first().id, false)
						}
					}
				}
			}

		}
	}

	override fun onItemLongClicked(position: Int, v: View?, item: ForumTopic.Message) {
		showTopicMenu(item)
	}

	private fun showTopicMenu(item: ForumTopic.Message) {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForTopicMessage(recycler.context, isLoggedIn(), item.creation.user.id.toInt() == PrefGetter.getLoggedUser()?.id), item, 0)
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onMessageDeleted(messageId: Int) {
		hideProgress()
		val position = adapter.data.indexOf(adapter.data.find { it.id.toInt() == messageId })
		adapter.data.removeAt(position)
		adapter.notifyItemRemoved(position)
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

		fun newInstance(topicId: Int, topicTitle: String, forumId: Int, isClosed: Boolean): TopicFragment {
			//TODO fix uselles code
			val view = TopicFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, topicId)
					.put(BundleConstant.EXTRA_TWO, topicTitle)
					.put(BundleConstant.EXTRA_THREE, forumId)
					.put(BundleConstant.YES_NO_EXTRA, isClosed)
					.end()
			return view
		}
	}

}
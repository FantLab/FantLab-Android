package ru.fantlab.android.ui.modules.forums.topic

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.ForumTopic
import ru.fantlab.android.data.dao.response.ForumTopicResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.TopicMessagesSortOption
import ru.fantlab.android.provider.rest.getTopicMessagesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class TopicPresenter : BasePresenter<TopicMvp.View>(),
		TopicMvp.Presenter {

	private var page: Int = 1
	private var topicId: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE
	private var previousTotal: Int = 0
	private var order = TopicMessagesSortOption.BY_NEW

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		topicId = parameter!!
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		getMessages(false)
		return true
	}

	override fun getMessages(force: Boolean) {
		makeRestCall(
				getTopicsInternal(force).toObservable(),
				Consumer { (pinned, messages, lastPage) ->
					sendToView {
						this.lastPage = lastPage
						it.getLoadMore().setTotalPagesCount(lastPage)
						it.onSetPinnedMessage(pinned)
						it.onNotifyAdapter(messages, page)
					}
				}
		)
	}

	override fun refreshMessages(lastMessageId: String, isNewMessage: Boolean) {
		makeRestCall(
				getLastMessages().toObservable(),
				Consumer { messages ->
					sendToView {
						val newMessages = messages.filter { message -> message.id > lastMessageId } as ArrayList<ForumTopic.Message>
						if (isNewMessage && newMessages.size > 1)
							it.onAddToAdapter(newMessages, isNewMessage)
						else if (!isNewMessage && newMessages.isNotEmpty())
							it.onAddToAdapter(newMessages, isNewMessage)
						else
							it.hideProgress()
					}
				}
		)
	}

	private fun getTopicsInternal(force: Boolean) =
			getTopicsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getMessagesFromDb()
						} else {
							throw throwable
						}
					}

	private fun getTopicsFromServer(): Single<Triple<ForumTopic.PinnedMessage?, ArrayList<ForumTopic.Message>, Int>> =
			DataManager.getTopicMessages(topicId, page, order, 20)
					.map { getMessages(it) }

	private fun getMessagesFromDb(): Single<Triple<ForumTopic.PinnedMessage?, ArrayList<ForumTopic.Message>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getTopicMessagesPath(topicId, page, order, 20))
					.map { it.response }
					.map { ForumTopicResponse.Deserializer().deserialize(it) }
					.map { getMessages(it) }

	private fun getLastMessages(): Single<ArrayList<ForumTopic.Message>> =
			DataManager.getTopicMessages(topicId, 1, TopicMessagesSortOption.BY_NEW, 10)
					.map { it.messages.items }

	private fun getMessages(response: ForumTopicResponse): Triple<ForumTopic.PinnedMessage?, ArrayList<ForumTopic.Message>, Int> = Triple(response.pinnedMessage, response.messages.items, response.messages.last)

	fun onDeleteMessage(messageId: Int) {
		makeRestCall(
				DataManager.deleteTopicMessage(messageId).toObservable(),
				Consumer { _ -> sendToView { it.onMessageDeleted(messageId) } }
		)
	}

	override fun onDeleteDraftMessage(topicId: Int) {
		makeRestCall(
				DataManager.deleteTopicDraft(topicId).toObservable(),
				Consumer { sendToView {
					it.onMessageDraftDeleted()
					it.hideProgress()
				} }
		)
	}

	override fun onConfirmDraftMessage(topicId: Int, lastMessageId: String) {
		makeRestCall(
				DataManager.confirmTopicDraft(topicId).toObservable(),
				Consumer {
					sendToView { it.onMessageDraftDeleted() }
					refreshMessages(lastMessageId, false)
				}
		)
	}

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: ForumTopic.Message) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: ForumTopic.Message) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}
package ru.fantlab.android.ui.adapter.viewholder

import android.view.*
import kotlinx.android.synthetic.main.topic_message_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.model.ForumTopic
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class TopicMessagesViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ForumTopic.Message, TopicMessagesViewHolder>)
	: BaseViewHolder<ForumTopic.Message>(itemView, adapter) {

	val list = object : ActionMode.Callback {

		override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
			menu.clear()
			menu.add(0, QuoteItemId, 0, itemView.context.getString(R.string.quote)).setIcon(R.drawable.ic_format_quote)
			menu.add(0, CopyItemId, 1, itemView.context.getString(R.string.copy)).setIcon(R.drawable.copy)
			return true
		}

		override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
			return true
		}

		override fun onDestroyActionMode(mode: ActionMode) {
		}

		override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
			when (item.itemId) {
				QuoteItemId -> {
					val message = adapter.getItem(adapterPosition)
					listener?.onShowEditor("[q=${message.creation.user.login}]${getSelection().toString().replace("\n", "")}[/q]\n")
					mode.finish()
					return true
				}
				CopyItemId -> {
					ActivityHelper.copyToClipboard(itemView.context, getSelection().toString())
					mode.finish()
					return true
				}
				else -> {
				}
			}
			return false
		}

	}

	fun getSelection(): CharSequence {
		var min = 0
		var max = itemView.messageText.text.length
		if (itemView.messageText.isFocused) {
			val selStart = itemView.messageText.selectionStart
			val selEnd = itemView.messageText.selectionEnd

			min = Math.max(0, Math.min(selStart, selEnd))
			max = Math.max(0, Math.max(selStart, selEnd))
		}
		return itemView.messageText.text.subSequence(min, max)
	}

	override fun bind(message: ForumTopic.Message) {
		itemView.userAvatar.setUrl(message.creation.user.avatar)
		val gender = if (message.creation.user.gender == "GENDER_MALE") "\u2642" else "\u2640"
		itemView.username.text = "${message.creation.user.login} $gender"
		itemView.userClass.text = FantlabHelper.classToName(message.creation.user.classX)
		itemView.messageDate.text = message.creation.date.parseFullDate(true).getTimeAgo()
		if (message.isCensored == true) {
			itemView.messageText.html = "[censor]${message.text}[/censor]"
		} else itemView.messageText.html = message.text

		if (!message.creation.user.sign.isNullOrEmpty()) {
			itemView.userSign.html = message.creation.user.sign
			itemView.divider.visibility = View.VISIBLE
			itemView.userSign.visibility = View.VISIBLE
		} else {
			itemView.divider.visibility = View.GONE
			itemView.userSign.visibility = View.GONE
		}

		itemView.messageText.customSelectionActionModeCallback = list

		itemView.replyButton.setOnClickListener { listener?.onShowEditor("[b]${message.creation.user.login}[/b], ") }
	}

	interface MessageMenu {
		fun onOpenContextMenu(topicMessage: Forum.Topic)
		fun onShowEditor(quoteText: String)
	}

	companion object {
		private var listener: MessageMenu? = null
		private var QuoteItemId = 1353
		private var CopyItemId = 1354

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<ForumTopic.Message, TopicMessagesViewHolder>
		): TopicMessagesViewHolder {
			return TopicMessagesViewHolder(getView(viewGroup, R.layout.topic_message_row_item), adapter)
		}

		fun setOnMessageMenuListener(listener: MessageMenu) {
			this.listener = listener
		}


	}
}
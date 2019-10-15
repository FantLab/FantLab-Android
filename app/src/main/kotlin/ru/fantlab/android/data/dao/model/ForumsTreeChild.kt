package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class ForumsTreeChild(
		var title: String,
		var lastTopic: String,
		var lastDate: String,
		var lastUsername: String,
		var lastUsernameId: Int,
		var messagesCount: Int,
		var topicsCount: Int,
		var forumId: Int
) : LayoutItemType {

	override val layoutId = R.layout.forums_child_row_item

}
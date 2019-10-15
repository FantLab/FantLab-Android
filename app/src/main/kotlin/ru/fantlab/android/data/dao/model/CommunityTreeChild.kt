package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class CommunityTreeChild(
		var avatar: String,
		var title: String,
		var articleCount: Int,
		var subscriberCount: Int,
		var lastArticleDate: String,
		var lastUsernameLogin: String,
		var lastUsernameId: Int,
		var lastArticleTitle: String,
		var communityId: Int
) : LayoutItemType {

	override val layoutId = R.layout.community_child_row_item

}
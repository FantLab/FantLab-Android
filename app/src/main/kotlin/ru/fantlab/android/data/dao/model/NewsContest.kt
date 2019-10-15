package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class NewsContest(var title: String, var description: String?, val linkType: String, var workId: Int, val place: String) : LayoutItemType {

	override val layoutId = R.layout.contest_row_item

}
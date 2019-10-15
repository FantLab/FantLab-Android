package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class WorkAwardsChild(
		var awardId: Int,
		var nameRus: String,
		var nameOrig: String,
		var country: String,
		var type: String?,
		var date: Int
) : LayoutItemType {

	override val layoutId = R.layout.work_awards_child_row_item

}
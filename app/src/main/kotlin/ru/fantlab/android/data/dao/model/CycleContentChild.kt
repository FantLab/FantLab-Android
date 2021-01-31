package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class CycleContentChild(val title: String, val workId: Int, val rating: Float, val responses: Int) : LayoutItemType {

	override val layoutId = R.layout.cycle_content_child_row_item

}
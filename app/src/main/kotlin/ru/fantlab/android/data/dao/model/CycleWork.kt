package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class CycleWork(var title: String, var workId: Int) : LayoutItemType {

	override val layoutId = R.layout.author_cycle_work_row_item

}
package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class Consts(var title: String, var description: String?, var workId: Int) : LayoutItemType {

	override val layoutId = R.layout.consts_work_row_item

}
package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class ConstsParent(var title: String, var description: String?) : LayoutItemType {

	override val layoutId = R.layout.consts_parent_row_item

}
package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class ClassParent(var title: String, var percent: Float?) : LayoutItemType {

	override val layoutId = R.layout.work_classif_parent_row_item

}
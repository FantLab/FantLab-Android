package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class Classificator(var name: String, var description: String?, var id: Int) : LayoutItemType {

	override val layoutId = R.layout.classif_parent_row_item

}
package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class BookcaseChild(var name: String, var description: String, var type: String) : LayoutItemType {

    override val layoutId = R.layout.bookcase_row_item

}
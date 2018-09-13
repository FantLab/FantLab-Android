package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

@Parcelize
class CycleWork(
		var work: ChildWork,
		var mark: Int?
) : LayoutItemType, Parcelable {

	override val layoutId = R.layout.author_cycle_work_row_item

}
package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.adapter.viewholder.SettingsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class SettingsAdapter constructor(settings: ArrayList<SettingsModel>)
	: BaseRecyclerAdapter<SettingsModel, SettingsViewHolder, BaseViewHolder.OnItemClickListener<SettingsModel>>(settings) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder
			= SettingsViewHolder.newInstance(parent, this)

	override fun onBindView(holderEdition: SettingsViewHolder, position: Int) {
		holderEdition.bind(getItem(position))
	}
}
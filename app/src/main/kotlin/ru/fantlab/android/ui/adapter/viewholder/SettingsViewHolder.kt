package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.setting_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SettingsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SettingsModel, SettingsViewHolder>)
	: BaseViewHolder<SettingsModel>(itemView, adapter) {

	override fun bind(setting: SettingsModel) {
		itemView.iconItemImage.setImageResource(setting.image)
		itemView.iconItemTitle.text = setting.title
		if (setting.summary.isNotEmpty()) {
			itemView.iconItemSummary.text = setting.summary
		} else {
			itemView.iconItemSummary.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<SettingsModel, SettingsViewHolder>
		): SettingsViewHolder =
				SettingsViewHolder(getView(viewGroup, R.layout.setting_row_item), adapter)
	}
}
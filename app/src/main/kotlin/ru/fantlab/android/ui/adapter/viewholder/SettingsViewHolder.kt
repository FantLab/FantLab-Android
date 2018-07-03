package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SettingsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SettingsModel, SettingsViewHolder, *>)
	: BaseViewHolder<SettingsModel>(itemView, adapter) {

	@BindView(R.id.iconItemImage) lateinit var icon: ForegroundImageView
	@BindView(R.id.iconItemTitle) lateinit var title: FontTextView
	@BindView(R.id.iconItemSummary) lateinit var summary: FontTextView

	override fun bind(setting: SettingsModel) {
		icon.setImageResource(setting.image)
		title.text = setting.title
        if (setting.summary.isNotEmpty()) {summary.text = setting.summary} else {summary.visibility = View.GONE}
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SettingsModel, SettingsViewHolder, *>) : SettingsViewHolder
				= SettingsViewHolder(getView(viewGroup, R.layout.setting_row_item), adapter)
	}
}
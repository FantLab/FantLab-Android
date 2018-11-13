package ru.fantlab.android.ui.modules.settings

import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SettingsMvp {

	interface View : BaseMvp.View {

		fun onItemClicked(item: SettingsModel)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SettingsModel> {

		fun getSettings(): ArrayList<SettingsModel>
	}
}
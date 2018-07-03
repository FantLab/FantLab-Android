package ru.fantlab.android.ui.modules.settings

import android.view.View
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class SettingsPresenter : BasePresenter<SettingsMvp.View>(), SettingsMvp.Presenter {

    private var settings: ArrayList<SettingsModel> = ArrayList()


    override fun onItemClick(position: Int, v: View?, item: SettingsModel) {
        view?.onItemClicked(item)
    }

    override fun onItemLongClick(position: Int, v: View?, item: SettingsModel) {
    }

    override fun getSettings(): ArrayList<SettingsModel> = settings
}
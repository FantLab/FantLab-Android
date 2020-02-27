package ru.fantlab.android.ui.modules.translator.works

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

interface TranslatorWorksMvp {

    interface View : BaseMvp.View,
            SwipeRefreshLayout.OnRefreshListener,
            android.view.View.OnClickListener,
            ContextMenuDialogView.ListDialogViewActionCallback {

        fun onTranslatorInformationRetrieved(translatedWorks: HashMap<String, Translator.TranslatedWork>)
    }

    interface Presenter : BaseMvp.Presenter {

        fun getTranslatorWorks(id: Int)
    }
}
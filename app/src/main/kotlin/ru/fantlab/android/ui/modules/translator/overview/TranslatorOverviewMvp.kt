package ru.fantlab.android.ui.modules.translator.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface TranslatorOverviewMvp {

    interface View : BaseMvp.View {

        fun onTranslatorInformationRetrieved(translator: Translator)

        fun onShowErrorView(msgRes: String?)

    }

    interface Presenter : BaseMvp.Presenter {

        fun onFragmentCreated(bundle: Bundle)
    }
}
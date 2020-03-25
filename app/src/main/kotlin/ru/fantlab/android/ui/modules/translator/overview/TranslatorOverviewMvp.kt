package ru.fantlab.android.ui.modules.translator.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface TranslatorOverviewMvp {

    interface View : BaseMvp.View {

        fun onTranslatorInformationRetrieved(translator: Translator, awards: ArrayList<Translator.TranslationAward>)

        fun onShowErrorView(msgRes: String?)

        fun onItemClicked(item: Nomination)
    }

    interface Presenter : BaseMvp.Presenter,
            BaseViewHolder.OnItemClickListener<Nomination> {

        fun onFragmentCreated(bundle: Bundle)
    }
}
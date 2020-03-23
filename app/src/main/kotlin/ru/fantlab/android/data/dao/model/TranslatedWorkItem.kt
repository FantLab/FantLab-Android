package ru.fantlab.android.data.dao.model

import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.treeview.LayoutItemType

class TranslatedWorkItem(var translatedWork: Translator.TranslatedWork) : LayoutItemType {

    override val layoutId = R.layout.translated_work_row_item

}
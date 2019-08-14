package ru.fantlab.android.ui.modules.bookcases.editor

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import ru.fantlab.android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import es.dmoral.toasty.Toasty
import ru.fantlab.android.ui.base.BaseActivity
import kotlinx.android.synthetic.main.bookcase_editor_layout.*
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.InputHelper
import java.util.ArrayList

class BookcaseEditorActivty : BaseActivity<BookcaseEditorMvp.View, BookcaseEditorPresenter>(), BookcaseEditorMvp.View {

    private var categories: ArrayList<Pair<String, String>> = arrayListOf()

    override fun isTransparent(): Boolean = true

    override fun providePresenter(): BookcaseEditorPresenter = BookcaseEditorPresenter()

    override fun layout(): Int = R.layout.bookcase_editor_layout

    override fun canBack(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categories.add(Pair("work", getString(R.string.bookcase_work_type)))
        categories.add(Pair("edition", getString(R.string.bookcase_edition_type)))
        categories.add(Pair("films", getString(R.string.bookcase_film_type)))

        bookcaseEditSave.setOnClickListener {
            presenter.createBookcase(convertTypeName(bookcaseType.selectedItem.toString()),
                    InputHelper.toString(bookcaseName),
                    bookcasePublic.isChecked,
                    InputHelper.toString(bookcaseDescription))
        }
        bookcaseEditCancel.setOnClickListener {
            finish()
        }
        bookcaseType.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories.map { it.second })
        title = getString(R.string.bookcase_creation_title)
    }

    override fun onEmptyBookcaseName(isEmpty: Boolean) {
        bookcaseName.error = if (isEmpty) getString(R.string.required_field) else null
    }

    override fun onSuccessfullyCreated() {
        hideProgress()
        Toasty.info(applicationContext!!, getString(R.string.bookcase_created), Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onSuccessfullyUpdated() {
        hideProgress()
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showErrorMessage(msgRes: String?) {
        hideProgress()
        super.showErrorMessage(msgRes)
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        AnimHelper.animateVisibility(progress, true)
    }

    private fun convertTypeName(typeName: String): String {
        categories.forEach {
            if (it.second.equals(typeName)) {
                return it.first
            }
        }

        return ""
    }
}
package ru.fantlab.android.ui.modules.bookcases.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import ru.fantlab.android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.evernote.android.state.State
import es.dmoral.toasty.Toasty
import ru.fantlab.android.ui.base.BaseActivity
import kotlinx.android.synthetic.main.bookcase_editor_layout.*
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.modules.bookcases.viewer.BookcaseViewerActivity
import java.util.ArrayList

class BookcaseEditorActivty : BaseActivity<BookcaseEditorMvp.View, BookcaseEditorPresenter>(), BookcaseEditorMvp.View {
    private var editorMode: Boolean = false

    private var categories: ArrayList<Pair<String, String>> = arrayListOf()
    private var storedBookcaseId: Int = 0

    override fun isTransparent(): Boolean = true

    override fun providePresenter(): BookcaseEditorPresenter = BookcaseEditorPresenter()

    override fun layout(): Int = R.layout.bookcase_editor_layout

    override fun canBack(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editorMode = intent?.extras?.getBoolean(BundleConstant.EXTRA, false) ?: false

        if (editorMode) {
            storedBookcaseId = intent?.extras?.getInt(BundleConstant.EXTRA_TWO, -1) ?: -1
        }
        if (editorMode && storedBookcaseId == -1) {
            finish()
            return
        }

        categories.add(Pair("work", getString(R.string.bookcase_work_type)))
        categories.add(Pair("edition", getString(R.string.bookcase_edition_type)))
        categories.add(Pair("film", getString(R.string.bookcase_film_type)))

        bookcaseEditCancel.setOnClickListener {
            finish()
        }
        bookcaseType.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories.map { it.second })
        if (editorMode) {
            title = getString(R.string.bookcase_update_title)

            bookcaseName.editText?.setText(intent?.extras?.getString(BundleConstant.EXTRA_THREE, "") ?: "")
            bookcaseDescription.editText?.setText(intent?.extras?.getString(BundleConstant.EXTRA_FIVE, "") ?: "")
            bookcasePublic.isChecked = ((intent?.extras?.getInt(BundleConstant.EXTRA_SIX, 0) ?: 1) == 1)
            bookcaseType.setSelection(categories.indexOf(getTypeObject(intent?.extras?.getString(BundleConstant.EXTRA_FOUR, "") ?: "")))
            bookcaseType.isEnabled = false

            bookcaseEditSave.setOnClickListener {
                presenter.updateBookcase(storedBookcaseId,
                        convertTypeName(bookcaseType.selectedItem.toString()),
                        InputHelper.toString(bookcaseName),
                        bookcasePublic.isChecked,
                        InputHelper.toString(bookcaseDescription))
            }

        } else {
            title = getString(R.string.bookcase_creation_title)
            bookcaseEditSave.setOnClickListener {
                presenter.createBookcase(convertTypeName(bookcaseType.selectedItem.toString()),
                        InputHelper.toString(bookcaseName),
                        bookcasePublic.isChecked,
                        InputHelper.toString(bookcaseDescription))
            }
        }
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
        Toasty.info(applicationContext!!, getString(R.string.bookcase_updated), Toast.LENGTH_LONG).show()
        val intent = Intent(this, BookcaseEditorActivty::class.java)
        intent
                .putExtra(BundleConstant.EXTRA_THREE, InputHelper.toString(bookcaseName))
                .putExtra(BundleConstant.EXTRA_FIVE, InputHelper.toString(bookcaseDescription))
                .putExtra(BundleConstant.EXTRA_SIX, if (bookcasePublic.isChecked) 1 else 0)
        setResult(Activity.RESULT_OK, intent)
        finish()
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

    private fun getTypeObject(type: String): Pair<String, String> {
        categories.forEach {
            if (it.first.equals(type)) {
                return it
            }
        }

        return Pair("work", getString(R.string.bookcase_work_type))
    }
}
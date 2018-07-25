package ru.fantlab.android.ui.modules.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.transition.TransitionManager
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import butterknife.BindView
import butterknife.OnClick
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.provider.markdown.MarkDownProvider
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.dialog.MessageDialogView
import ru.fantlab.android.ui.widgets.markdown.MarkDownLayout
import ru.fantlab.android.ui.widgets.markdown.MarkdownEditText
import java.util.*

class EditorActivity : BaseActivity<EditorMvp.View, EditorPresenter>(), EditorMvp.View {

	private var participants: ArrayList<String>? = null
    @BindView(R.id.replyQuote) lateinit var replyQuote: LinearLayout
    @BindView(R.id.replyQuoteText) lateinit var quote: FontTextView
    @BindView(R.id.markDownLayout) lateinit var markDownLayout: MarkDownLayout
    @BindView(R.id.editText) lateinit var editText: MarkdownEditText
    @BindView(R.id.list_divider) lateinit var listDivider: View
    @BindView(R.id.parentView) lateinit var parentView: View
    @BindView(R.id.autocomplete) lateinit var mention: ListView

    @State var extraType: String? = null
    @State var itemId: Int? = null
    @State var reviewComment: Response? = null

    override fun layout(): Int = R.layout.editor_layout

    override fun isTransparent(): Boolean = false

    override fun canBack(): Boolean = true

    override fun providePresenter(): EditorPresenter = EditorPresenter()

    @OnClick(R.id.replyQuoteText) internal fun onToggleQuote() {
        TransitionManager.beginDelayedTransition((parentView as ViewGroup))
        if (quote.maxLines == 3) {
            quote.maxLines = Integer.MAX_VALUE
        } else {
            quote.maxLines = 3
        }
        quote.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                if (quote.maxLines == 3) R.drawable.ic_arrow_drop_down
                else R.drawable.ic_arrow_drop_up, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		if (!isLoggedIn()) finish()
        markDownLayout.markdownListener = this
        setToolbarIcon(R.drawable.ic_clear)
        if (savedInstanceState == null) {
            onCreate()
        }
        invalidateOptionsMenu()
        editText.initListView(mention, listDivider, participants)
        editText.requestFocus()
    }

    override fun onSendResultAndFinish(commentModel: Response, isNew: Boolean) {
        hideProgress()
        val intent = Intent()
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ITEM, commentModel)
                .put(BundleConstant.EXTRA, isNew)
                .end())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSendMarkDownResult() {
        val intent = Intent()
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, editText.savedText).end())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

	override fun onSendMessageResult(result: String) {
		hideProgress()
		onSendMarkDownResult()
	}

	override fun onSendReviewResultAndFinish(comment: Response, isNew: Boolean) {
        hideProgress()
        val intent = Intent()
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ITEM, comment)
                .put(BundleConstant.EXTRA, isNew)
                .end())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.submit) {
            presenter.onHandleSubmission(editText.savedText, extraType, itemId, reviewComment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (menu.findItem(R.id.submit) != null) {
            menu.findItem(R.id.submit).isEnabled = true
        }
        if (BundleConstant.FOR_RESULT_EXTRA.equals(extraType, ignoreCase = true)) {
            menu.findItem(R.id.submit).setIcon(R.drawable.ic_done)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        super.showProgress(resId, cancelable)
        invalidateOptionsMenu()
    }

    override fun hideProgress() {
        invalidateOptionsMenu()
        super.hideProgress()
    }

    override fun onBackPressed() {
        if (!InputHelper.isEmpty(editText)) {
            ViewHelper.hideKeyboard(editText)
            MessageDialogView.newInstance(getString(R.string.close), getString(R.string.unsaved_data_warning), false,
                    Bundler.start()
                            .put("primary_extra", getString(R.string.discard))
                            .put("secondary_extra", getString(R.string.cancel))
                            .put(BundleConstant.EXTRA, true)
                            .end())
                    .show(supportFragmentManager, MessageDialogView.TAG)
            return
        }
        super.onBackPressed()
    }

    override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {
        super.onMessageDialogActionClicked(isOk, bundle)
        if (isOk && bundle != null) {
            finish()
        }
    }

    override fun onAppendLink(title: String, link: String, isLink: Boolean) {
        markDownLayout.onAppendLink(title, link, isLink)
    }

    override fun onSmileAdded(smile: Smile?) {
        markDownLayout.onSmileAdded(smile)
    }

    override fun getEditText(): EditText = editText

    override fun getSavedText(): CharSequence? = editText.savedText

    override fun fragmentManager(): FragmentManager = supportFragmentManager

    private fun onCreate() {
        if (intent != null && intent.extras != null) {
            val bundle = intent.extras
            extraType = bundle.getString(BundleConstant.EXTRA_TYPE)
            reviewComment = bundle.getParcelable(BundleConstant.REVIEW_EXTRA)
            itemId = bundle.getInt(BundleConstant.ID)
            val textToUpdate = bundle.getString(BundleConstant.EXTRA)
            if (!InputHelper.isEmpty(textToUpdate)) {
                editText.setText(String.format("%s ", textToUpdate))
                editText.setSelection(InputHelper.toString(editText).length)
            }
            if (bundle.getString("message", "").isBlank()) {
                replyQuote.visibility = GONE
            } else {
                MarkDownProvider.setMdText(quote, bundle.getString("message", ""))
            }
            participants = bundle.getStringArrayList("participants")
            when (extraType){
                BundleConstant.EDITOR_NEW_REVIEW -> {
                    markDownLayout.visibility = View.INVISIBLE
                }
                BundleConstant.EDITOR_NEW_COMMENT -> {
                    markDownLayout.editorIconsHolder.visibility = View.INVISIBLE
                }
            }
        }
    }
}
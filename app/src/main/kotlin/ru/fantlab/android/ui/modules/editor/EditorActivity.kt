package ru.fantlab.android.ui.modules.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.editor_buttons_layout.view.*
import kotlinx.android.synthetic.main.editor_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.dialog.MessageDialogView
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView

class EditorActivity : BaseActivity<EditorMvp.View, EditorPresenter>(), EditorMvp.View {

	@State var extraType: String? = null
	@State var extraId: Int? = null
	@State var itemId: Int? = null
	@State var extraPosition: Int = -1
	@State var reviewComment: Response? = null

	override fun layout(): Int = R.layout.editor_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): EditorPresenter = EditorPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (!isLoggedIn()) finish()
		editorLayout.editorListener = this
		title = getString(R.string.editor)
		setToolbarIcon(R.drawable.ic_clear)
		if (savedInstanceState == null) {
			onCreate()
		}
		invalidateOptionsMenu()
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

	override fun onSendEditorResult() {
		val intent = Intent()
		intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, editText.savedText).put(BundleConstant.ID, extraPosition).end())
		setResult(Activity.RESULT_OK, intent)
		finish()
	}

	override fun onSendMessageResult(result: String) {
		hideProgress()
		onSendEditorResult()
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
			if (extraType != BundleConstant.EDITOR_NEW_COMMENT) {
				if (extraType == BundleConstant.EDITOR_NEW_RESPONSE && editText.savedText.length < 50) {
					showErrorMessage(getString(R.string.response_short_text))
					return true
				} else if (editText.savedText.isBlank()) {
					showErrorMessage(getString(R.string.too_short_text))
					return true
				}
				MessageDialogView.newInstance(getString(R.string.select_action), getString(R.string.save_hint), false,
						Bundler.start()
								.put("primary_extra", getString(R.string.submit))
								.put("secondary_extra", getString(R.string.save))
								.put(BundleConstant.EXTRA_TYPE, extraType!!)
								.end())
						.show(supportFragmentManager, MessageDialogView.TAG)
			} else presenter.onHandleSubmission(editText.savedText, extraType, itemId, reviewComment, "")
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
		if (bundle != null) {
			val type = bundle.getString(BundleConstant.EXTRA_TYPE)
			if (type == null) {
				if (isOk) finish()
			} else {
				presenter.onHandleSubmission(editText.savedText, extraType, itemId, reviewComment, if (isOk) "send" else "preview")
			}
		}
	}

	override fun onAppendLink(title: String, link: String, isLink: Boolean) {
		editorLayout.onAppendLink(title, link, isLink)
	}

	override fun onSmileAdded(smile: Smile?) {
		editorLayout.onSmileAdded(smile)
	}

	override fun getEditText(): EditText = editText

	override fun getHtmlsText(): HTMLTextView = htmlText

	override fun getSavedText(): CharSequence? = editText.savedText

	override fun getCurrentType(): String? = extraType

	override fun getExtraIds(): Int? = extraId

	override fun fragmentManager(): FragmentManager = supportFragmentManager

	private fun onCreate() {
		if (intent != null && intent.extras != null) {
			val bundle = intent.extras
			extraType = bundle.getString(BundleConstant.EXTRA_TYPE)
			reviewComment = bundle.getParcelable(BundleConstant.REVIEW_EXTRA)
			itemId = bundle.getInt(BundleConstant.ID)
			val textToUpdate = bundle.getString(BundleConstant.EXTRA)
			if (!InputHelper.isEmpty(textToUpdate)) {
				extraId = bundle.getInt(BundleConstant.EXTRA_TWO)
				editText.setText(String.format("%s ", textToUpdate))
				editText.setSelection(InputHelper.toString(editText).length)
			}
			extraPosition = bundle.getInt(BundleConstant.EXTRA_THREE)
			when (extraType) {
				BundleConstant.EDITOR_NEW_RESPONSE,
				BundleConstant.EDITOR_EDIT_RESPONSE -> {
					title = getString(R.string.editor_response)
					editorLayout.addSmile.visibility = GONE
					editorLayout.bold.visibility = GONE
					editorLayout.strikethrough.visibility = GONE
					editorLayout.italic.visibility = GONE
					editorLayout.underlined.visibility = GONE
					editorLayout.quote.visibility = GONE
					editorLayout.list.visibility = GONE
					editorLayout.link.visibility = GONE
					editorLayout.image.visibility = GONE
				}
				BundleConstant.EDITOR_NEW_COMMENT -> {
					title = getString(R.string.editor_comment)
					editorLayout.editorIconsHolder.visibility = View.INVISIBLE
				}
				BundleConstant.EDITOR_NEW_MESSAGE -> {
					title = getString(R.string.editor_message)
				}
			}
		}
	}
}
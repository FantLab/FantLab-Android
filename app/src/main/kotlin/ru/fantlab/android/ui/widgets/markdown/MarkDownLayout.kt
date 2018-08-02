package ru.fantlab.android.ui.widgets.markdown

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v4.app.FragmentManager
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.provider.markdown.MarkDownProvider
import ru.fantlab.android.ui.modules.editor.popup.EditorLinkImageDialogFragment
import ru.fantlab.android.ui.modules.editor.smiles.SmileBottomSheet
import ru.fantlab.android.ui.modules.editor.smiles.SmileMvp


class MarkDownLayout : LinearLayout, SmileMvp.SmileCallback {

	var markdownListener: MarkdownListener? = null
    var selectionIndex = 0
    @BindView(R.id.editorIconsHolder) lateinit var editorIconsHolder: HorizontalScrollView
    @BindView(R.id.addSmile) lateinit var addEmojiView: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        orientation = HORIZONTAL
        View.inflate(context, R.layout.markdown_buttons_layout, this)
        if (isInEditMode) return
        ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        markdownListener = null
        super.onDetachedFromWindow()
    }

    @OnClick(R.id.view) fun onViewMarkDown() {
        markdownListener?.let {
            it.getEditText().let { editText ->
                TransitionManager.beginDelayedTransition(this)
                if (editText.isEnabled && !InputHelper.isEmpty(editText)) {
                    editText.isEnabled = false
                    selectionIndex = editText.selectionEnd
                    MarkDownProvider.setMdText(editText, InputHelper.toString(editText))

                    editorIconsHolder.visibility = View.INVISIBLE
                    addEmojiView.visibility = View.INVISIBLE
                    ViewHelper.hideKeyboard(editText)
                } else {
                    editText.setText(it.getSavedText())
                    editText.setSelection(selectionIndex)
                    editText.isEnabled = true
					if (markdownListener?.getCurrentType() == BundleConstant.EDITOR_NEW_COMMENT)
						editorIconsHolder.visibility = View.INVISIBLE
					else
						editorIconsHolder.visibility = View.VISIBLE
                    addEmojiView.visibility = View.VISIBLE
					ViewHelper.showKeyboard(editText)
                }
            }
        }
    }

    @OnClick(R.id.spoiler, R.id.bold, R.id.italic, R.id.strikethrough,
            R.id.list, R.id.quote, R.id.link, R.id.image, R.id.addSmile)
    fun onActions(v: View) {
        markdownListener?.let {
            it.getEditText().let { editText ->
                if (!editText.isEnabled) {
                    Snackbar.make(this, R.string.error_highlighting_editor, Snackbar.LENGTH_SHORT).show()
                } else {
                    when {
                        v.id == R.id.link -> EditorLinkImageDialogFragment.newInstance(true, getSelectedText())
                                .show(it.fragmentManager(), "EditorLinkImageDialogFragment")
                        v.id == R.id.image -> EditorLinkImageDialogFragment.newInstance(false, getSelectedText())
                                .show(it.fragmentManager(), "EditorLinkImageDialogFragment")
                        v.id == R.id.addSmile -> {
                            ViewHelper.hideKeyboard(it.getEditText())
									SmileBottomSheet().show(it.fragmentManager(), "SmileBottomSheet")
                        }
                        else -> onActionClicked(editText, v.id)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onActionClicked(editText: EditText, id: Int) {
        if (editText.selectionEnd == -1 || editText.selectionStart == -1) {
            return
        }
        when (id) {
            R.id.spoiler -> MarkDownProvider.addSpoiler(editText)
            R.id.bold -> MarkDownProvider.addBold(editText)
            R.id.italic -> MarkDownProvider.addItalic(editText)
            R.id.strikethrough -> MarkDownProvider.addStrikeThrough(editText)
            R.id.list -> MarkDownProvider.addList(editText, "<li>")
            R.id.quote -> MarkDownProvider.addQuote(editText)
        }
    }

    interface MarkdownListener {
        fun getEditText(): EditText
        fun fragmentManager(): FragmentManager
        fun getSavedText(): CharSequence?
        fun getCurrentType(): String?
    }

    fun onAppendLink(title: String, link: String, isLink: Boolean) {
        markdownListener?.let {
            if (isLink) {
                MarkDownProvider.addLink(it.getEditText(), title, link)
            } else {
                MarkDownProvider.addPhoto(it.getEditText(), title, link)
            }
        }
    }

    override fun onSmileAdded(smile: Smile?) {
        markdownListener?.getEditText()?.let { editText ->
            ViewHelper.showKeyboard(editText)
            smile?.let {
                MarkDownProvider.insertAtCursor(editText, ":${it.id}:")
            }
        }
    }

    private fun getSelectedText(): String {
        markdownListener?.getEditText()?.let {
            if (!it.text.toString().isBlank()) {
                val selectionStart = it.selectionStart
                val selectionEnd = it.selectionEnd
                return it.text.toString().substring(selectionStart, selectionEnd)
            }
        }
        return ""
    }
}
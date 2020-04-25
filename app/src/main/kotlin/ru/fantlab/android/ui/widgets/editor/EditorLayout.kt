package ru.fantlab.android.ui.widgets.editor

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.editor_buttons_layout.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.modules.editor.popup.attach.EditorAttachDialogFragment
import ru.fantlab.android.ui.modules.editor.popup.linkimage.EditorLinkImageDialogFragment
import ru.fantlab.android.ui.modules.editor.smiles.SmileBottomSheet
import ru.fantlab.android.ui.modules.editor.smiles.SmileMvp
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView

class EditorLayout : LinearLayout, SmileMvp.SmileCallback {

	var editorListener: EditorListener? = null
	var selectionIndex = 0

	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun onFinishInflate() {
		super.onFinishInflate()
		orientation = HORIZONTAL
		View.inflate(context, R.layout.editor_buttons_layout, this)
		if (isInEditMode) return
		view.setOnClickListener { onViewEditor() }
		spoiler.setOnClickListener { onActions(it) }
		bold.setOnClickListener { onActions(it) }
		italic.setOnClickListener { onActions(it) }
		underlined.setOnClickListener { onActions(it) }
		strikethrough.setOnClickListener { onActions(it) }
		list.setOnClickListener { onActions(it) }
		quote.setOnClickListener { onActions(it) }
		link.setOnClickListener { onActions(it) }
		image.setOnClickListener { onActions(it) }
		file.setOnClickListener { onActions(it) }
		addSmile.setOnClickListener { onActions(it) }
	}

	override fun onDetachedFromWindow() {
		editorListener = null
		super.onDetachedFromWindow()
	}

	private fun onViewEditor() {
		editorListener?.let {
			it.getEditText().let { editText ->
				TransitionManager.beginDelayedTransition(this)

				if (editText.isEnabled && !InputHelper.isEmpty(editText)) {
					view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_eye_off))
					it.getHtmlsText().html = it.getSavedText()
					editText.isEnabled = false
					editText.visibility = View.GONE
					it.getHtmlsText().visibility = View.VISIBLE
					selectionIndex = editText.selectionEnd
					editorIconsHolder.visibility = View.INVISIBLE
					if (addSmile.visibility == View.VISIBLE) addSmile.visibility = View.INVISIBLE
					ViewHelper.hideKeyboard(editText)
				} else {
					view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_eye))
					editText.setText(it.getSavedText())
					editText.setSelection(selectionIndex)
					editText.isEnabled = true
					it.getHtmlsText().visibility = View.GONE
					editText.visibility = View.VISIBLE
					if (editorListener?.getCurrentType() == BundleConstant.EDITOR_NEW_COMMENT)
						editorIconsHolder.visibility = View.INVISIBLE
					else
						editorIconsHolder.visibility = View.VISIBLE
					if (addSmile.visibility == View.INVISIBLE) addSmile.visibility = View.VISIBLE
					ViewHelper.showKeyboard(editText)
				}
			}
		}
	}

	fun onActions(v: View) {
		editorListener?.let {
			it.getEditText().let { editText ->
				if (!editText.isEnabled) {
					Snackbar.make(this, R.string.error_highlighting_editor, Snackbar.LENGTH_SHORT).show()
				} else {
					when (v.id) {
						R.id.link -> EditorLinkImageDialogFragment.newInstance(true, getSelectedText())
								.show(it.fragmentManager(), "EditorLinkImageDialogFragment")
						R.id.image -> EditorLinkImageDialogFragment.newInstance(false, getSelectedText())
								.show(it.fragmentManager(), "EditorLinkImageDialogFragment")
						R.id.addSmile -> {
							ViewHelper.hideKeyboard(it.getEditText())
							SmileBottomSheet().show(it.fragmentManager(), "SmileBottomSheet")
						}
						R.id.file -> {
							EditorAttachDialogFragment.newInstance().show(it.fragmentManager(), "EditorAttachDialogFragment")
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
			R.id.spoiler -> if (editorListener?.getCurrentType() == BundleConstant.EDITOR_NEW_RESPONSE || editorListener?.getCurrentType() == BundleConstant.EDITOR_EDIT_RESPONSE) addSpoiler(editText) else addHidden(editText)
			R.id.bold -> addBold(editText)
			R.id.italic -> addItalic(editText)
			R.id.underlined -> addUnderlined(editText)
			R.id.strikethrough -> addStrikeThrough(editText)
			R.id.list -> addList(editText, "[*]")
			R.id.quote -> addQuote(editText)
		}
	}

	interface EditorListener {
		fun getEditText(): EditText
		fun getHtmlsText(): HTMLTextView
		fun fragmentManager(): FragmentManager
		fun getSavedText(): CharSequence?
		fun getCurrentType(): String?
		fun getExtraIds(): Int?
	}

	fun onAppendLink(title: String, link: String, isLink: Boolean) {
		editorListener?.let {
			if (isLink) {
				addLink(it.getEditText(), title, link)
			} else {
				addPhoto(it.getEditText(), title, link)
			}
		}
	}

	override fun onSmileAdded(smile: Smile?) {
		editorListener?.getEditText()?.let { editText ->
			ViewHelper.showKeyboard(editText)
			smile?.let {
				insertAtCursor(editText, ":${it.id}:")
			}
		}
	}

	private fun getSelectedText(): String {
		editorListener?.getEditText()?.let {
			if (!it.text.toString().isBlank()) {
				val selectionStart = it.selectionStart
				val selectionEnd = it.selectionEnd
				return it.text.toString().substring(selectionStart, selectionEnd)
			}
		}
		return ""
	}

	private fun addList(editText: EditText, list: String) {
		val tag = "$list "
		val source = editText.text.toString()
		var selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		var substring = source.substring(0, selectionStart)
		val line = substring.lastIndexOf(10.toChar())
		selectionStart = if (line != -1) {
			line + 1
		} else {
			0
		}
		substring = source.substring(selectionStart, selectionEnd)
		val split = substring.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val stringBuffer = StringBuilder()
		stringBuffer.append("[list]")
		if (split.isNotEmpty())
			for (s in split) {
				if (s.isEmpty() && stringBuffer.isNotEmpty()) {
					stringBuffer.append("\n")
					continue
				}
				if (!s.trim { it <= ' ' }.startsWith(tag)) {
					if (stringBuffer.isNotEmpty()) stringBuffer.append("\n")
					stringBuffer.append(tag).append(s)
				} else {
					if (stringBuffer.isNotEmpty()) stringBuffer.append("\n")
					stringBuffer.append(s)
				}
			}

		if (stringBuffer.isEmpty()) {
			stringBuffer.append(tag)
		}
		stringBuffer.append("\n")
		stringBuffer.append("[/list]")
		editText.text.replace(selectionStart, selectionEnd, stringBuffer.toString())
		editText.setSelection(stringBuffer.length + selectionStart)
	}

	private fun addItalic(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[i]$substring[/i]"
		if (substring.contains("[i]") && substring.contains("[/i]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[i\\]|\\[\\/i\\]".toRegex(), ""))
			return
		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	private fun addUnderlined(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[u]$substring[/u]"

		if (substring.contains("[u]") && substring.contains("[/u]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[u\\]|\\[\\/u\\]".toRegex(), ""))
			return
		}

		if (source.contains(result)) return
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	private fun addBold(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[b]$substring[/b]"
		if (substring.contains("[b]") && substring.contains("[/b]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[b\\]|\\[\\/b\\]".toRegex(), ""))
			return
		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	private fun addSpoiler(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[spoiler]$substring[/spoiler]"
		if (substring.contains("[spoiler]") && substring.contains("[/spoiler]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[spoiler\\]|\\[\\/spoiler\\]".toRegex(), ""))
			return
		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 10)
	}

	private fun addHidden(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[h]$substring[/h]"
		if (substring.contains("[h]") && substring.contains("[/h]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[h\\]|\\[\\/h\\]".toRegex(), ""))
			return
		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	private fun addStrikeThrough(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result = "[s]$substring[/s]"
		if (substring.contains("[s]") && substring.contains("[/s]")) {
			editText.text.replace(selectionStart, selectionEnd, substring.replace("\\[s\\]|\\[\\/s\\]".toRegex(), ""))
			return
		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart - 4)
	}

	private fun addQuote(editText: EditText) {
		val source = editText.text.toString()
		val selectionStart = editText.selectionStart
		val selectionEnd = editText.selectionEnd
		val substring = source.substring(selectionStart, selectionEnd)
		val result: String
		result = if (hasNewLine(source, selectionStart)) {
			"[q]$substring[/q]\n"
		} else {
			"\n[q]$substring[/q]\n"

		}
		editText.text.replace(selectionStart, selectionEnd, result)
		editText.setSelection(result.length + selectionStart)
	}

	private fun addPhoto(editText: EditText, title: String, link: String) {
		val result = "\n[IMG]$link[/IMG]\n"
		insertAtCursor(editText, result)
	}

	private fun addLink(editText: EditText, title: String, link: String) {
		val result = "[url=$link]$title[/url]"
		insertAtCursor(editText, result)
	}

	private fun hasNewLine(source: String, selectionStart: Int): Boolean {
		var source = source
		try {
			if (source.isEmpty()) return true
			source = source.substring(0, selectionStart)
			return source[source.length - 1].toInt() == 10
		} catch (e: StringIndexOutOfBoundsException) {
			return false
		}
	}

	fun insertAtCursor(editText: EditText, text: String) {
		val oriContent = editText.text.toString()
		val start = editText.selectionStart
		val end = editText.selectionEnd
		if (start >= 0 && end > 0 && start != end) {
			editText.text = editText.text.replace(start, end, text)
		} else {
			val index = if (editText.selectionStart >= 0) editText.selectionStart else 0
			val builder = StringBuilder(oriContent)
			builder.insert(index, text)
			editText.setText(builder.toString())
			editText.setSelection(index + text.length)
		}
	}
}
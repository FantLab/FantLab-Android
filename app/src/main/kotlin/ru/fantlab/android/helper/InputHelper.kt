package ru.fantlab.android.helper

import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.widget.EditText

/**
 * Created by kosh20111 on 3/11/2015. CopyRights @
 * <p>
 * Input Helper to validate stuff related to input fields.
 */
object InputHelper {

	private fun isWhiteSpaces(s: String?): Boolean {
		return s != null && s.matches("\\s+".toRegex())
	}

	fun isEmpty(text: String?): Boolean {
		return text == null || TextUtils.isEmpty(text) || isWhiteSpaces(text) || text.equals("null", ignoreCase = true)
	}

	fun isEmpty(text: Any?): Boolean {
		return text == null || isEmpty(text.toString())
	}

	fun toString(textInputLayout: TextInputLayout?): String {
		return if (textInputLayout?.editText != null) toString(textInputLayout.editText) else ""
	}

	fun toString(editText: EditText?): String {
		return editText?.text.toString()
	}
}
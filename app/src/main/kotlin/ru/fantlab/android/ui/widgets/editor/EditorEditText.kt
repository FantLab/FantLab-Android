package ru.fantlab.android.ui.widgets.editor

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import ru.fantlab.android.ui.widgets.FontEditText

class EditorEditText : FontEditText {

	var savedText: CharSequence = ""

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter)
		if (isEnabled) {
			savedText = text
		}
	}

	@SuppressLint("SetTextI18n")
	override fun setText(text: CharSequence, type: TextView.BufferType) {
		try {
			super.setText(text, type)
		} catch (e: Exception) {
			setText("I tried, but your OEM just sucks because they modify the framework components and therefore causing the app to crash!" + "" +
					".\nFantLab")
			Crashlytics.logException(e)
		}

	}
}

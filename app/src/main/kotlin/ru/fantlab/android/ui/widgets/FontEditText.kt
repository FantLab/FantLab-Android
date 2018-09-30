package ru.fantlab.android.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import ru.fantlab.android.helper.TypeFaceHelper

open class FontEditText : AppCompatEditText {

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()

	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	private fun init() {
		if (isInEditMode) return
		inputType = inputType or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
		imeOptions = imeOptions or EditorInfo.IME_FLAG_NO_FULLSCREEN
		TypeFaceHelper.applyTypeface(this)
	}

	@SuppressLint("SetTextI18n")
	override fun setText(text: CharSequence, type: TextView.BufferType) {
		try {
			super.setText(text, type)
		} catch (e: Exception) {
			setText("I tried, but your OEM just sucks because they modify the framework components and therefore causing the app to crash!" + ".\nFantLab")
			Crashlytics.logException(e)
		}

	}
}
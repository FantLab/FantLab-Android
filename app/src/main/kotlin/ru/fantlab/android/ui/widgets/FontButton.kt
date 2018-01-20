package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import ru.fantlab.android.helper.TypeFaceHelper

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
class FontButton : AppCompatButton {

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
		TypeFaceHelper.applyTypeface(this)
	}
}
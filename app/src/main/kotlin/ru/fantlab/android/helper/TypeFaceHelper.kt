package ru.fantlab.android.helper

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

/**
 * Created by Kosh on 17/12/15 10:25 PM
 */
object TypeFaceHelper {

	var typeface: Typeface? = null
		private set

	fun generateTypeface(context: Context) {
		typeface = Typeface.createFromAsset(context.assets, "fonts/app_font.ttf")
	}

	fun applyTypeface(textView: TextView) {
		textView.typeface = typeface
	}
}

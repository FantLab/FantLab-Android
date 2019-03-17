package ru.fantlab.android.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceViewHolder
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import petrov.kristiyan.colorpicker.ColorPicker
import ru.fantlab.android.R
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.ViewHelper

class ColorPickerPreference : Preference, ColorPicker.OnChooseColorListener {

	lateinit var holder: PreferenceViewHolder
	private val selectedColor: Int?
		get() {
			val colorTypedArray = context.resources.obtainTypedArray(R.array.theme_colors_hex)
			val colorNames = context.resources.getStringArray(R.array.theme_colors)

			val colors = ArrayList<Int>()
			for (i in 0 until colorTypedArray.length()) {
				colors.add(colorTypedArray.getColor(i, 0))
			}
			colorTypedArray.recycle()
			val preferenceValueToColor: HashMap<Int, Int> = HashMap()

			for (i in colorNames.indices) {
				preferenceValueToColor[PrefGetter.getThemeColor(context.resources, colorNames[i])] = colors.get(i)
			}
			return preferenceValueToColor[PrefGetter.getThemeColor(context)]
		}

	private val selectedColorName: String
		get() {
			val colorNames = context.resources.getStringArray(R.array.theme_colors)
			return colorNames[PrefGetter.getThemeColor(context) - 1]
		}

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	private fun init() {
		widgetLayoutResource = R.layout.preference_widget_color
	}

	override fun onClick() {
		super.onClick()
		val selectedColor = selectedColor ?: -1
		val title = String.format(context.getString(R.string.currentColor), selectedColorName)
		val colorPicker = ColorPicker(context)
		colorPicker.setRoundColorButton(true)
		colorPicker.setColors(R.array.theme_colors_hex)
		colorPicker.setDefaultColorButton(selectedColor)
		colorPicker.setTitle(title)
		val title_tv: TextView = colorPicker.dialogViewLayout.findViewById(R.id.title)
		title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
		colorPicker.positiveButton.setTextColor(ViewHelper.getPrimaryTextColor(context))
		colorPicker.negativeButton.setTextColor(ViewHelper.getPrimaryTextColor(context))
		colorPicker.setOnChooseColorListener(this)
		colorPicker.show()
	}

	override fun onBindViewHolder(holder: PreferenceViewHolder) {
		super.onBindViewHolder(holder)
		this.holder = holder
		val colorButton = holder.findViewById(R.id.color) as Button
		colorButton.setBackgroundResource(R.drawable.circle_shape)
		colorButton.background.setColorFilter(selectedColor ?: -1, PorterDuff.Mode.SRC_IN)
	}

	override fun onChooseColor(position: Int, color: Int) {
		val colorButton = holder.findViewById(R.id.color) as Button
		colorButton.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
		persistString(context.resources.getStringArray(R.array.theme_colors)[position])
		onPreferenceChangeListener.onPreferenceChange(this, context.resources.getStringArray(R.array.theme_colors)[position])
	}

	override fun onCancel() {
	}
}
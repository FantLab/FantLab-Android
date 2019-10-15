package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.picker_dialog.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.AppFontScaleModel
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.base.BaseBottomSheetDialog
import kotlin.reflect.KFunction0


class FontScaleBottomSheetDialog : BaseBottomSheetDialog() {

	private var callback: FontScaleDialogViewActionCallback? = null

	interface FontScaleDialogViewActionCallback {
		fun onFontChanged(action: KFunction0<Unit>)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment != null && parentFragment is FontScaleDialogViewActionCallback) {
			callback = parentFragment as FontScaleDialogViewActionCallback
		} else if (context is FontScaleDialogViewActionCallback) {
			callback = context
		}
	}

	override fun onDetach() {
		super.onDetach()
		callback = null
	}

	override fun layoutRes(): Int {
		return R.layout.picker_dialog
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val fontScale = PrefGetter.getAppFontScale()
		val values = resources.getStringArray(R.array.fonts_array_values)
		val padding = resources.getDimensionPixelSize(R.dimen.spacing_xs_large)
		title.setText(R.string.font)
		resources.getStringArray(R.array.fonts_array)
				.mapIndexed { index, string -> AppFontScaleModel(values[index].toFloat(), string) }
				.forEachIndexed { index, appFontScaleModel ->
					val radioButtonView = RadioButton(context)
					val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
					radioButtonView.layoutParams = params
					radioButtonView.text = appFontScaleModel.label
					radioButtonView.id = index
					radioButtonView.tag = appFontScaleModel.value
					radioButtonView.gravity = Gravity.CENTER_VERTICAL
					radioButtonView.setPadding(padding, padding, padding, padding)
					picker.addView(radioButtonView)
					if (appFontScaleModel.value == fontScale) picker.check(index)
				}
		picker.setOnCheckedChangeListener { group, checkedId ->
			val tag = picker.getChildAt(checkedId).tag as Float
			if (tag != fontScale) {
				PrefGetter.setAppFontScale(tag)
				callback?.onFontChanged(this::dismiss)
			}
		}
	}

}
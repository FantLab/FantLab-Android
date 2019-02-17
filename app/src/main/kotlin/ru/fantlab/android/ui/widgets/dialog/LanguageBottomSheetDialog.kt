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
import ru.fantlab.android.data.dao.AppLanguageModel
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.base.BaseBottomSheetDialog
import kotlin.reflect.KFunction0


class LanguageBottomSheetDialog : BaseBottomSheetDialog() {

	private var callback: LanguageDialogViewActionCallback? = null

	interface LanguageDialogViewActionCallback {
		fun onLanguageChanged(action: KFunction0<Unit>)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment != null && parentFragment is LanguageDialogViewActionCallback) {
			callback = parentFragment as LanguageDialogViewActionCallback
		} else if (context is LanguageDialogViewActionCallback) {
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
		val language = PrefGetter.getAppLanguage()
		val values = resources.getStringArray(R.array.languages_array_values)
		val padding = resources.getDimensionPixelSize(R.dimen.spacing_xs_large)
		resources.getStringArray(R.array.languages_array)
				.mapIndexed { index, string -> AppLanguageModel(values[index], string) }
				.sortedBy { it.label }
				.forEachIndexed { index, appLanguageModel ->
					val radioButtonView = RadioButton(context)
					val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
					radioButtonView.layoutParams = params
					radioButtonView.text = appLanguageModel.label
					radioButtonView.id = index
					radioButtonView.tag = appLanguageModel.value
					radioButtonView.gravity = Gravity.CENTER_VERTICAL
					radioButtonView.setPadding(padding, padding, padding, padding)
					picker.addView(radioButtonView)
					if (appLanguageModel.value.equals(language, ignoreCase = true)) picker.check(index)
				}
		picker.setOnCheckedChangeListener { group, checkedId ->
			val tag = picker.getChildAt(checkedId).tag as String
			if (!tag.equals(language, ignoreCase = true)) {
				PrefGetter.setAppLanguage(tag)
				callback?.onLanguageChanged(this::dismiss)
			}
		}
	}

}
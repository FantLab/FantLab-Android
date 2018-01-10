package ru.fantlab.android.ui.widgets.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseDialogFragment
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProgressDialogFragment : BaseDialogFragment<BaseMvp.View, BasePresenter<BaseMvp.View>>() {

	companion object {
		val TAG: String = ProgressDialogFragment::class.java.simpleName

		fun newInstance(msg: String, isCancelable: Boolean): ProgressDialogFragment {
			val fragment = ProgressDialogFragment()
			fragment.arguments = Bundler.start()
					.put("msg", msg)
					.put("isCancelable", isCancelable)
					.end()
			return fragment
		}
	}

	init {
		suppressAnimation = true
	}

	override fun fragmentLayout(): Int {
		return R.layout.progress_dialog_layout
	}

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = super.onCreateDialog(savedInstanceState)
		dialog.setCancelable(false)
		isCancelable = false
		dialog.window?.let {
			it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
			it.setDimAmount(0f)
		}
		return dialog
	}

	override fun providePresenter(): BasePresenter<BaseMvp.View> {
		return BasePresenter()
	}
}
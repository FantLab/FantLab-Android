package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.message_dialog.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseBottomSheetDialog

open class MessageDialogView : BaseBottomSheetDialog() {

	interface MessageDialogViewActionCallback {

		fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?)

		fun onDialogDismissed()
	}

	private var callback: MessageDialogViewActionCallback? = null

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment != null && parentFragment is MessageDialogViewActionCallback) {
			callback = parentFragment as MessageDialogViewActionCallback
		} else if (context is MessageDialogViewActionCallback) {
			callback = context
		}
	}

	override fun onDetach() {
		super.onDetach()
		callback = null
	}

	override fun layoutRes(): Int {
		return R.layout.message_dialog
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val bundle = arguments
		title.text = bundle?.getString("bundleTitle")
		val msg = bundle?.getString("bundleMsg")
		message.text = msg
		bundle?.let {
			val hideCancel = it.getBoolean("hideCancel")
			if (hideCancel) cancel.visibility = View.GONE
			initButton(it)
		}
		ok.setOnClickListener {
			callback?.let {
				isAlreadyHidden = true
				it.onMessageDialogActionClicked(true, arguments?.getBundle("bundle"))
			}
			dismiss()
		}
		cancel.setOnClickListener {
			callback?.let {
				isAlreadyHidden = true
				it.onMessageDialogActionClicked(false, arguments?.getBundle("bundle"))
			}
			dismiss()
		}
	}

	private fun initButton(bundle: Bundle) {
		val extra = bundle.getBundle("bundle")
		if (extra != null) {
			val yesNo = extra.getBoolean(BundleConstant.YES_NO_EXTRA)
			if (yesNo) {
				ok.setText(R.string.yes)
				cancel.setText(R.string.no)
			} else {
				val hideButtons = extra.getBoolean("hide_buttons")
				val primaryExtra = extra.getString("primary_extra")
				val secondaryExtra = extra.getString("secondary_extra")
				if (hideButtons) {
					ok.visibility = View.GONE
					cancel.visibility = View.GONE
				} else if (!InputHelper.isEmpty(primaryExtra)) {
					ok.text = primaryExtra
					if (!InputHelper.isEmpty(secondaryExtra)) cancel.text = secondaryExtra
					ok.visibility = View.VISIBLE
					cancel.visibility = View.VISIBLE
				}
			}
		}
	}

	override fun onDismissedByScrolling() {
		super.onDismissedByScrolling()
		callback?.onDialogDismissed()
	}

	override fun onHidden() {
		callback?.onDialogDismissed()
		super.onHidden()
	}

	companion object {

		val TAG: String = MessageDialogView::class.java.simpleName

		fun newInstance(bundleTitle: String, bundleMsg: String, hideCancel: Boolean = false, bundle: Bundle? = null): MessageDialogView {
			val messageDialogView = MessageDialogView()
			messageDialogView.arguments = getBundle(bundleTitle, bundleMsg, bundle, hideCancel)
			return messageDialogView
		}

		private fun getBundle(bundleTitle: String, bundleMsg: String, bundle: Bundle?, hideCancel: Boolean): Bundle {
			return Bundler.start()
					.put("bundleTitle", bundleTitle)
					.put("bundleMsg", bundleMsg)
					.put("bundle", bundle)
					.put("hideCancel", hideCancel)
					.end()
		}

		fun getYesNoBundle(context: Context): Bundle {
			return Bundler.start()
					.put("primary_extra", context.getString(R.string.yes))
					.put("secondary_extra", context.getString(R.string.no))
					.end()
		}
	}
}
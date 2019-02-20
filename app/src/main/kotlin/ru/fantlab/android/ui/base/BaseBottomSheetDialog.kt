package ru.fantlab.android.ui.base

import android.annotation.TargetApi
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import com.evernote.android.state.StateSaver
import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper
import android.support.v7.view.ContextThemeWrapper;

/**
 * Created by Kosh on 16 Sep 2016, 2:11 PM
 */

abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {

	protected var bottomSheetBehavior: BottomSheetBehavior<View>? = null
	private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
		override fun onStateChanged(bottomSheet: View, newState: Int) {
			if (newState == BottomSheetBehavior.STATE_HIDDEN) {
				isAlreadyHidden = true
				onHidden()
			}
		}

		override fun onSlide(bottomSheet: View, slideOffset: Float) {
			if (slideOffset.toDouble() == -1.0) {
				isAlreadyHidden = true
				onDismissedByScrolling()
			}
		}
	}
	protected var isAlreadyHidden: Boolean = false

	@LayoutRes
	protected abstract fun layoutRes(): Int

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		StateSaver.saveInstanceState(this, outState)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState != null && !savedInstanceState.isEmpty) {
			StateSaver.restoreInstanceState(this, savedInstanceState)
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val contextThemeWrapper = ContextThemeWrapper(context, context?.theme)
		val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
		val view = themeAwareInflater.inflate(layoutRes(), container, false)
		view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				view.viewTreeObserver.removeOnGlobalLayoutListener(this)
				val parent = dialog.findViewById<View>(R.id.design_bottom_sheet)
				parent?.let {
					bottomSheetBehavior = BottomSheetBehavior.from(it)
					bottomSheetBehavior?.let {
						with(it) {
							setBottomSheetCallback(bottomSheetCallback)
							state = BottomSheetBehavior.STATE_EXPANDED
						}
					}
				}
			}
		})
		return view
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = super.onCreateDialog(savedInstanceState)
		dialog.setOnShowListener {
			if (ViewHelper.isTablet(activity)) {
				dialog.window?.setLayout(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.MATCH_PARENT
				)
			}
			onDialogIsShowing()
		}
		dialog.setOnKeyListener { _, keyCode, _ ->
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				isAlreadyHidden = true
				onDismissedByScrolling()
			}
			false
		}
		return dialog
	}

	override fun onDetach() {
		if (!isAlreadyHidden) {
			onDismissedByScrolling()
		}
		super.onDetach()
	}

	open fun onHidden() {
		try {
			dismiss()
		} catch (ignored: IllegalStateException) {
		}
	}

	open fun onDismissedByScrolling() {}

	private fun onDialogIsShowing() {}
}
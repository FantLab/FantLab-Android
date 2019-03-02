package ru.fantlab.android.ui.modules.editor.popup

import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.editor_link_image_dialog_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseDialogFragment

class EditorLinkImageDialogFragment : BaseDialogFragment<EditorLinkImageMvp.View, EditorLinkImagePresenter>(), EditorLinkImageMvp.View {

	private var callbacks: EditorLinkImageMvp.EditorLinkCallback? = null

	init {
		suppressAnimation = true
	}

	private val isLink: Boolean
		get() = arguments != null && arguments!!.getBoolean(BundleConstant.YES_NO_EXTRA)

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment is EditorLinkImageMvp.EditorLinkCallback) {
			callbacks = parentFragment as EditorLinkImageMvp.EditorLinkCallback?
		} else if (context is EditorLinkImageMvp.EditorLinkCallback) {
			callbacks = context
		}
	}

	override fun onDetach() {
		callbacks = null
		super.onDetach()
	}

	override fun onUploaded(title: String, link: String) {
		hideProgress()
		if (callbacks != null) {
			callbacks!!.onAppendLink(title, link.replace("http:", "https:"), isLink)
		}
		dismiss()
	}

	override fun fragmentLayout() = R.layout.editor_link_image_dialog_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			link_title.editText!!.setText(arguments!!.getString(BundleConstant.ITEM))
			if (!isLink) {
				link_title.isEnabled = false
				link_title.hint = getString(R.string.no_title)
			}
		}
		cancel.setOnClickListener { dismiss() }
		insert.setOnClickListener { onInsertClicked() }
	}

	override fun providePresenter(): EditorLinkImagePresenter {
		return EditorLinkImagePresenter()
	}

	private fun onInsertClicked() {
		if (callbacks != null) {
			callbacks!!.onAppendLink(InputHelper.toString(link_title), InputHelper.toString(link_link), isLink)
		}
		dismiss()
	}

	override fun onMessageDialogActionClicked(
			isOk: Boolean, bundle: Bundle?
	) {

	}

	override fun onDialogDismissed() {

	}

	fun newInstance(): EditorLinkImageDialogFragment {
		return EditorLinkImageDialogFragment()
	}

	companion object {

		fun newInstance(isLink: Boolean, link: String): EditorLinkImageDialogFragment {
			val fragment = EditorLinkImageDialogFragment()
			fragment.arguments = Bundler
					.start()
					.put(BundleConstant.YES_NO_EXTRA, isLink)
					.put(BundleConstant.ITEM, link)
					.end()
			return fragment
		}
	}
}

package ru.fantlab.android.ui.modules.editor.popup

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseDialogFragment

class EditorLinkImageDialogFragment : BaseDialogFragment<EditorLinkImageMvp.View, EditorLinkImagePresenter>(), EditorLinkImageMvp.View {

	@BindView(R.id.link_title) lateinit var title: TextInputLayout
	@BindView(R.id.link_link) lateinit var link: TextInputLayout

	private var callbacks: EditorLinkImageMvp.EditorLinkCallback? = null

	init {
		suppressAnimation = true
	}

	private val isLink: Boolean
		get() = arguments != null && arguments!!.getBoolean(BundleConstant.YES_NO_EXTRA)

	override fun onAttach(context: Context?) {
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

	override fun fragmentLayout() = R.layout.markdown_link_image_dialog_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			title.editText!!.setText(arguments!!.getString(BundleConstant.ITEM))
			if (!isLink) {
				title.isEnabled = false
				title.hint = getString(R.string.no_title)
			}
		}
	}

	override fun providePresenter(): EditorLinkImagePresenter {
		return EditorLinkImagePresenter()
	}

	@OnClick(R.id.cancel)
	fun onCancelClicked() {
		dismiss()
	}

	@OnClick(R.id.insert)
	fun onInsertClicked() {
		if (callbacks != null) {
			callbacks!!.onAppendLink(InputHelper.toString(title), InputHelper.toString(link), isLink)
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

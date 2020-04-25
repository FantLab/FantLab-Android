package ru.fantlab.android.ui.modules.editor.popup.attach

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.editor_attach_dialog_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.helper.FantlabHelper.Attaches.MaxAttachCountPerMessage
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.adapter.AttachesAdapter
import ru.fantlab.android.ui.adapter.viewholder.AttachesViewHolder
import ru.fantlab.android.ui.base.BaseDialogFragment
import ru.fantlab.android.ui.widgets.filepicker.controller.DialogSelectionListener
import ru.fantlab.android.ui.widgets.filepicker.model.DialogConfigs
import ru.fantlab.android.ui.widgets.filepicker.model.DialogProperties
import ru.fantlab.android.ui.widgets.filepicker.view.FilePickerDialog
import java.io.File


class EditorAttachDialogFragment : BaseDialogFragment<EditorAttachMvp.View, EditorAttachPresenter>(), EditorAttachMvp.View, AttachesViewHolder.OnClickMenu {

	override fun fragmentLayout() = R.layout.editor_attach_dialog_layout

	override fun providePresenter(): EditorAttachPresenter = EditorAttachPresenter()

	private var callbacks: EditorAttachMvp.EditorLinkCallback? = null

	private val adapter: AttachesAdapter by lazy { AttachesAdapter(arrayListOf()) }

	private val attaches: ArrayList<AttachModel>
		get() = callbacks?.getAttachesList() ?: arrayListOf()

	lateinit var dialog: FilePickerDialog

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment is EditorAttachMvp.EditorLinkCallback) {
			callbacks = parentFragment as EditorAttachMvp.EditorLinkCallback?
		} else if (context is EditorAttachMvp.EditorLinkCallback) {
			callbacks = context
		}
	}

	override fun onDetach() {
		callbacks = null
		super.onDetach()
	}

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		attachesList.adapter = adapter
		adapter.insertItems(attaches)
		emptyHint.isVisible = attaches.isEmpty()
		attachesList.isVisible = attaches.isNotEmpty()
		adapter.setOnClickMenuListener(this)
		attachButton.setOnClickListener { showSelectFileDialog() }
		close.setOnClickListener { dismiss() }
		clear.setOnClickListener { onClearAttach() }
		add.setOnClickListener { onAttachClicked() }
	}

	private fun showSelectFileDialog() {
		val properties = DialogProperties().apply {
			selection_mode = DialogConfigs.SINGLE_MODE
			selection_type = DialogConfigs.FILE_SELECT
			root = File(DialogConfigs.DEFAULT_DIR)
			error_dir = File(DialogConfigs.DEFAULT_DIR)
			offset = File(DialogConfigs.DEFAULT_DIR)
			extensions = null
			show_hidden_files = false
		}
		dialog = FilePickerDialog(activity!!, properties)
		dialog.setTitle(getString(R.string.attach_selector))
		dialog.setDialogSelectionListener(object : DialogSelectionListener {
			override fun onSelectedFilePaths(files: Array<String?>) {
				filePath.setText(files[0])
				add.isEnabled = true
			}
		})
		dialog.show()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (this::dialog.isInitialized) dialog.show()
				} else {
					showErrorMessage(getString(R.string.permissions_denied))
				}
			}
		}
	}

	private fun onClearAttach() {
		callbacks?.onClearAttach()
		adapter.data.clear()
		adapter.notifyDataSetChanged()
		attachesList.isVisible = false
		emptyHint.isVisible = true
	}

	private fun onAttachClicked() {
		if (callbacks != null) {
			if (attaches.size + 1 <= MaxAttachCountPerMessage) {
				val path = InputHelper.toString(filePath)
				val filename = path.substringAfterLast("/")
				if (attaches.find { it.filename == filename } != null) {
					showErrorMessage(getString(R.string.attach_already_exist))
				} else if (filename.isNotEmpty() && path.isNotEmpty()) {
					callbacks?.onAppendAttach(filename, path)
					adapter.addItem(AttachModel(filename, path))
					adapter.notifyDataSetChanged()
					emptyHint.isVisible = false
					attachesList.isVisible = true
					filePath.setText("")
					add.isEnabled = false
				} else showErrorMessage(getString(R.string.check_input))
			} else showErrorMessage(getString(R.string.attaches_max))
		}
	}

	override fun onMessageDialogActionClicked(
			isOk: Boolean, bundle: Bundle?
	) {
	}

	override fun onDialogDismissed() {
	}

	override fun onDelete(attach: AttachModel) {
		if (callbacks != null) {
			callbacks!!.removeAttach(attach)
			adapter.data.remove(attach)
			adapter.notifyDataSetChanged()
			attachesList.isVisible = attaches.isNotEmpty()
			emptyHint.isVisible = attaches.isEmpty()
		}
	}

	companion object {

		fun newInstance(): EditorAttachDialogFragment {
			return EditorAttachDialogFragment()
		}
	}

}

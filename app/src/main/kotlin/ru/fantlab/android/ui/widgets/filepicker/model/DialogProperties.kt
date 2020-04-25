package ru.fantlab.android.ui.widgets.filepicker.model

import java.io.File

class DialogProperties {
	var selection_mode: Int = DialogConfigs.SINGLE_MODE
	var selection_type: Int = DialogConfigs.FILE_SELECT
	var root: File = File(DialogConfigs.DEFAULT_DIR)
	var error_dir: File = File(DialogConfigs.DEFAULT_DIR)
	var offset: File = File(DialogConfigs.DEFAULT_DIR)
	var extensions: Array<String>? = null
	var show_hidden_files: Boolean = false

}
package ru.fantlab.android.ui.widgets.filepicker.utils

import ru.fantlab.android.ui.widgets.filepicker.model.DialogConfigs
import ru.fantlab.android.ui.widgets.filepicker.model.DialogProperties
import java.io.File
import java.io.FileFilter
import java.util.*

class ExtensionFilter(properties: DialogProperties) : FileFilter {
	private val validExtensions: Array<String>
	private val properties: DialogProperties
	override fun accept(file: File): Boolean {
		if (file.isDirectory && file.canRead()) {
			return true
		} else if (properties.selection_type == DialogConfigs.DIR_SELECT) {
			return false
		} else {
			val name = file.name.toLowerCase(Locale.getDefault())
			for (ext in validExtensions) {
				if (name.endsWith(ext)) {
					return true
				}
			}
		}
		return false
	}

	init {
		if (properties.extensions != null) {
			validExtensions = properties.extensions!!
		} else {
			validExtensions = arrayOf("")
		}
		this.properties = properties
	}
}
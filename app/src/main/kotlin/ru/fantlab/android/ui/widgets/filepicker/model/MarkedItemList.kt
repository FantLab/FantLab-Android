package ru.fantlab.android.ui.widgets.filepicker.model

import java.util.*

object MarkedItemList {
	private var ourInstance = HashMap<String?, FileListItem?>()
	fun addSelectedItem(item: FileListItem) {
		ourInstance[item.location] = item
	}

	fun removeSelectedItem(key: String?) {
		ourInstance.remove(key)
	}

	fun hasItem(key: String?): Boolean {
		return ourInstance.containsKey(key)
	}

	fun clearSelectionList() {
		ourInstance = HashMap()
	}

	fun addSingleFile(item: FileListItem) {
		ourInstance = HashMap()
		ourInstance[item.location] = item
	}

	val selectedPaths: Array<String?>
		get() {
			val paths: Set<String?> = ourInstance.keys
			val strings = arrayOfNulls<String>(paths.size)
			var i = 0
			for (path in paths) {
				strings[i++] = path
			}
			return strings
		}

	val fileCount: Int
		get() = ourInstance.size
}
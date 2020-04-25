package ru.fantlab.android.ui.widgets.filepicker.model

import java.util.*

class FileListItem : Comparable<FileListItem> {
	var filename = ""
	var location: String = ""
	var isDirectory = false
	var isMarked = false
	var time: Long = 0

	override fun compareTo(fileListItem: FileListItem): Int {
		return if (fileListItem.isDirectory && isDirectory) {
			filename.toLowerCase(Locale.ROOT).compareTo(fileListItem.filename.toLowerCase(Locale.getDefault()))
		} else if (!fileListItem.isDirectory && !isDirectory) {
			filename.toLowerCase(Locale.ROOT).compareTo(fileListItem.filename.toLowerCase(Locale.getDefault()))
		} else if (fileListItem.isDirectory && !isDirectory) {
			1
		} else {
			-1
		}
	}
}
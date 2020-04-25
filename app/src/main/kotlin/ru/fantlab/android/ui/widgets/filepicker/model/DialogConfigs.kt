package ru.fantlab.android.ui.widgets.filepicker.model

object DialogConfigs {
	const val SINGLE_MODE = 0
	const val MULTI_MODE = 1
	const val FILE_SELECT = 0
	const val DIR_SELECT = 1
	const val FILE_AND_DIR_SELECT = 2
	private const val DIRECTORY_SEPARATOR = "/"
	private const val STORAGE_DIR = "mnt"
	const val DEFAULT_DIR = DIRECTORY_SEPARATOR + STORAGE_DIR
}
package ru.fantlab.android.ui.widgets.filepicker.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import ru.fantlab.android.ui.widgets.filepicker.model.FileListItem
import java.io.File
import java.util.*

object Utility {
	fun checkStorageAccessPermissions(context: Context): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val permission = "android.permission.READ_EXTERNAL_STORAGE"
			val res = context.checkCallingOrSelfPermission(permission)
			res == PackageManager.PERMISSION_GRANTED
		} else {
			true
		}
	}

	fun prepareFileListEntries(internalList: ArrayList<FileListItem>, inter: File,
							   filter: ExtensionFilter?, show_hidden_files: Boolean): ArrayList<FileListItem> {
		var internalList = internalList
		try {
			for (name in Objects.requireNonNull(inter.listFiles(filter))) {
				if (name.canRead()) {
					if (name.name.startsWith(".") && !show_hidden_files) continue
					val item = FileListItem()
					item.filename = name.name
					item.isDirectory = name.isDirectory
					item.location = name.absolutePath
					item.time = name.lastModified()
					internalList.add(item)
				}
			}
			internalList.sort()
		} catch (e: NullPointerException) {
			e.printStackTrace()
			internalList = ArrayList()
		}
		return internalList
	}
}
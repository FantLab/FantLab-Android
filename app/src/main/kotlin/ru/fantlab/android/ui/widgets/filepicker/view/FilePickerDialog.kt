package ru.fantlab.android.ui.widgets.filepicker.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_file_list_item.*
import kotlinx.android.synthetic.main.dialog_footer.*
import kotlinx.android.synthetic.main.dialog_header.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.widgets.filepicker.controller.DialogSelectionListener
import ru.fantlab.android.ui.widgets.filepicker.controller.NotifyItemChecked
import ru.fantlab.android.ui.widgets.filepicker.controller.adapters.FileListAdapter
import ru.fantlab.android.ui.widgets.filepicker.model.DialogConfigs
import ru.fantlab.android.ui.widgets.filepicker.model.DialogProperties
import ru.fantlab.android.ui.widgets.filepicker.model.FileListItem
import ru.fantlab.android.ui.widgets.filepicker.model.MarkedItemList
import ru.fantlab.android.ui.widgets.filepicker.utils.ExtensionFilter
import ru.fantlab.android.ui.widgets.filepicker.utils.Utility
import ru.fantlab.android.ui.widgets.filepicker.widget.MaterialCheckbox
import java.io.File
import java.util.*


class FilePickerDialog : Dialog, AdapterView.OnItemClickListener {
	private val ctx: Context
	private var properties: DialogProperties
	private var callbacks: DialogSelectionListener? = null
	private var internalList: ArrayList<FileListItem>
	private var filter: ExtensionFilter
	private var mFileListAdapter: FileListAdapter? = null
	private var titleStr: String? = null
	private var positiveBtnNameStr: String? = null
	private var negativeBtnNameStr: String? = null
	private lateinit var listView: ListView

	constructor(context: Context) : super(context) {
		this.ctx = context
		properties = DialogProperties()
		filter = ExtensionFilter(properties)
		internalList = ArrayList()
	}

	constructor(context: Context, properties: DialogProperties, themeResId: Int) : super(context, themeResId) {
		this.ctx = context
		this.properties = properties
		filter = ExtensionFilter(properties)
		internalList = ArrayList()
	}

	constructor(context: Context, properties: DialogProperties) : super(context) {
		this.ctx = context
		this.properties = properties
		filter = ExtensionFilter(properties)
		internalList = ArrayList()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.dialog_main)
		listView = findViewById(R.id.fileList)
		val size = MarkedItemList.fileCount
		if (size == 0) {
			select.isEnabled = false
			val color: Int = ViewHelper.getAccentColor(ctx)
			select.setTextColor(Color.argb(128, Color.red(color), Color.green(color),
					Color.blue(color)))
		}
		if (negativeBtnNameStr != null) {
			cancel.text = negativeBtnNameStr
		}
		select.setOnClickListener {
			val paths = MarkedItemList.selectedPaths
			if (callbacks != null) {
				callbacks!!.onSelectedFilePaths(paths)
			}
			dismiss()
		}
		cancel.setOnClickListener { cancel() }
		mFileListAdapter = FileListAdapter(internalList, ctx, properties)
		mFileListAdapter?.setNotifyItemCheckedListener(object : NotifyItemChecked {
			override fun notifyCheckBoxIsClicked() {
				positiveBtnNameStr = if (positiveBtnNameStr == null) ctx.resources.getString(R.string.choose_button_label) else positiveBtnNameStr
				val size = MarkedItemList.fileCount
				if (size == 0) {
					select.isEnabled = false
					val color: Int = ViewHelper.getAccentColor(ctx)
					select.setTextColor(Color.argb(128, Color.red(color), Color.green(color),
							Color.blue(color)))
					select.text = positiveBtnNameStr
				} else {
					select.isEnabled = true
					val color: Int = ViewHelper.getAccentColor(ctx)
					select.setTextColor(color)
					val button_label = "$positiveBtnNameStr ($size) "
					select.text = button_label
				}
				if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
					mFileListAdapter!!.notifyDataSetChanged()
				}
			}

		})
		listView.adapter = mFileListAdapter
		setTitle()
	}

	private fun setTitle() {
		if (title == null || dname == null) {
			return
		}
		if (titleStr != null) {
			if (title!!.visibility == View.INVISIBLE) {
				title!!.visibility = View.VISIBLE
			}
			title!!.text = titleStr
			if (dname!!.visibility == View.VISIBLE) {
				dname!!.visibility = View.INVISIBLE
			}
		} else {
			if (title!!.visibility == View.VISIBLE) {
				title!!.visibility = View.INVISIBLE
			}
			if (dname!!.visibility == View.INVISIBLE) {
				dname!!.visibility = View.VISIBLE
			}
		}
	}

	override fun onStart() {
		super.onStart()
		positiveBtnNameStr = if (positiveBtnNameStr == null) ctx.resources.getString(R.string.choose_button_label) else positiveBtnNameStr
		select!!.text = positiveBtnNameStr
		if (Utility.checkStorageAccessPermissions(context)) {
			val currLoc: File
			internalList.clear()
			if (properties.offset.isDirectory && validateOffsetPath()) {
				currLoc = File(properties.offset.absolutePath)
				val parent = FileListItem()
				parent.filename = ctx.getString(R.string.label_parent_dir)
				parent.isDirectory = true
				parent.location = Objects.requireNonNull(currLoc.parentFile)
						.absolutePath
				parent.time = currLoc.lastModified()
				internalList.add(parent)
			} else if (properties.root.exists() && properties.root.isDirectory) {
				currLoc = File(properties.root.absolutePath)
			} else {
				currLoc = File(properties.error_dir.absolutePath)
			}
			dname!!.text = currLoc.name
			dir_path!!.text = currLoc.absolutePath
			setTitle()
			internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
					properties.show_hidden_files)
			mFileListAdapter!!.notifyDataSetChanged()
			listView.onItemClickListener = this
		}
	}

	private fun validateOffsetPath(): Boolean {
		val offsetPath = properties.offset.absolutePath
		val rootPath = properties.root.absolutePath
		return offsetPath != rootPath && offsetPath.contains(rootPath)
	}

	override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
		if (internalList.size > i) {
			val fitem = internalList[i]
			if (fitem.isDirectory) {
				if (File(fitem.location).canRead()) {
					val currLoc = File(fitem.location)
					dname!!.text = currLoc.name
					setTitle()
					dir_path!!.text = currLoc.absolutePath
					internalList.clear()
					if (currLoc.name != properties.root.name) {
						val parent = FileListItem()
						parent.filename = ctx.getString(R.string.label_parent_dir)
						parent.isDirectory = true
						parent.location = Objects.requireNonNull(currLoc
								.parentFile).absolutePath
						parent.time = currLoc.lastModified()
						internalList.add(parent)
					}
					internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
							properties.show_hidden_files)
					mFileListAdapter!!.notifyDataSetChanged()
				} else {
					Toast.makeText(ctx, R.string.error_dir_access,
							Toast.LENGTH_SHORT).show()
				}
			} else {
				val fmark: MaterialCheckbox = view.findViewById(R.id.file_mark)
				fmark.performClick()
			}
		}
	}

	fun getProperties(): DialogProperties {
		return properties
	}

	fun setProperties(properties: DialogProperties) {
		this.properties = properties
		filter = ExtensionFilter(properties)
	}

	fun setDialogSelectionListener(callbacks: DialogSelectionListener?) {
		this.callbacks = callbacks
	}

	override fun setTitle(titleStr: CharSequence) {
		this.titleStr = titleStr.toString()
		setTitle()
	}

	fun setPositiveBtnName(positiveBtnNameStr: CharSequence?) {
		if (positiveBtnNameStr != null) {
			this.positiveBtnNameStr = positiveBtnNameStr.toString()
		} else {
			this.positiveBtnNameStr = null
		}
	}

	fun setNegativeBtnName(negativeBtnNameStr: CharSequence?) {
		if (negativeBtnNameStr != null) {
			this.negativeBtnNameStr = negativeBtnNameStr.toString()
		} else {
			this.negativeBtnNameStr = null
		}
	}

	fun markFiles(paths: List<String?>?) {
		if (paths != null && paths.isNotEmpty()) {
			if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
				val temp = File(paths[0])
				when (properties.selection_type) {
					DialogConfigs.DIR_SELECT -> if (temp.exists() && temp.isDirectory) {
						val item = FileListItem()
						item.filename = temp.name
						item.isDirectory = temp.isDirectory
						item.isMarked = true
						item.time = temp.lastModified()
						item.location = temp.absolutePath
						MarkedItemList.addSelectedItem(item)
					}
					DialogConfigs.FILE_SELECT -> if (temp.exists() && temp.isFile) {
						val item = FileListItem()
						item.filename = temp.name
						item.isDirectory = temp.isDirectory
						item.isMarked = true
						item.time = temp.lastModified()
						item.location = temp.absolutePath
						MarkedItemList.addSelectedItem(item)
					}
					DialogConfigs.FILE_AND_DIR_SELECT -> if (temp.exists()) {
						val item = FileListItem()
						item.filename = temp.name
						item.isDirectory = temp.isDirectory
						item.isMarked = true
						item.time = temp.lastModified()
						item.location = temp.absolutePath
						MarkedItemList.addSelectedItem(item)
					}
				}
			} else {
				for (path in paths) {
					when (properties.selection_type) {
						DialogConfigs.DIR_SELECT -> {
							val temp = File(path)
							if (temp.exists() && temp.isDirectory) {
								val item = FileListItem()
								item.filename = temp.name
								item.isDirectory = temp.isDirectory
								item.isMarked = true
								item.time = temp.lastModified()
								item.location = temp.absolutePath
								MarkedItemList.addSelectedItem(item)
							}
						}
						DialogConfigs.FILE_SELECT -> {
							val temp = File(path)
							if (temp.exists() && temp.isFile) {
								val item = FileListItem()
								item.filename = temp.name
								item.isDirectory = temp.isDirectory
								item.isMarked = true
								item.time = temp.lastModified()
								item.location = temp.absolutePath
								MarkedItemList.addSelectedItem(item)
							}
						}
						DialogConfigs.FILE_AND_DIR_SELECT -> {
							val temp = File(path)
							if (temp.exists() && (temp.isFile || temp.isDirectory)) {
								val item = FileListItem()
								item.filename = temp.name
								item.isDirectory = temp.isDirectory
								item.isMarked = true
								item.time = temp.lastModified()
								item.location = temp.absolutePath
								MarkedItemList.addSelectedItem(item)
							}
						}
					}
				}
			}
		}
	}

	override fun show() {
		if (!Utility.checkStorageAccessPermissions(ctx)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				(ctx as Activity).requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), EXTERNAL_READ_PERMISSION_GRANT)
			}
		} else {
			super.show()
			positiveBtnNameStr = if (positiveBtnNameStr == null) ctx.resources.getString(R.string.choose_button_label) else positiveBtnNameStr
			select!!.text = positiveBtnNameStr
			val size = MarkedItemList.fileCount
			if (size == 0) {
				select!!.text = positiveBtnNameStr
			} else {
				val buttonLabel = "$positiveBtnNameStr ($size) "
				select!!.text = buttonLabel
			}
		}
	}

	override fun onBackPressed() {
		val currentDirName = dname!!.text.toString()
		if (internalList.size > 0) {
			val fitem = internalList[0]
			val currLoc = File(fitem.location)
			if (currentDirName == properties.root.name ||
					!currLoc.canRead()) {
				super.onBackPressed()
			} else {
				dname!!.text = currLoc.name
				dir_path!!.text = currLoc.absolutePath
				internalList.clear()
				if (currLoc.name != properties.root.name) {
					val parent = FileListItem()
					parent.filename = ctx.getString(R.string.label_parent_dir)
					parent.isDirectory = true
					parent.location = Objects.requireNonNull(currLoc.parentFile)
							.absolutePath
					parent.time = currLoc.lastModified()
					internalList.add(parent)
				}
				internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
						properties.show_hidden_files)
				mFileListAdapter!!.notifyDataSetChanged()
			}
			setTitle()
		} else {
			super.onBackPressed()
		}
	}

	override fun dismiss() {
		MarkedItemList.clearSelectionList()
		internalList.clear()
		super.dismiss()
	}

	companion object {
		const val EXTERNAL_READ_PERMISSION_GRANT = 112
	}
}
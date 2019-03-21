package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.simple_list_dialog.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.adapter.ContextListAdapter
import ru.fantlab.android.ui.base.BaseBottomSheetDialog
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ContextMenuDialogView : BaseBottomSheetDialog(), BaseViewHolder.OnItemClickListener<ContextMenus.MenuItem> {

	private var callbacks: ListDialogViewActionCallback? = null
	private lateinit var id: String
	private lateinit var listItem: Any
	private var positionItem: Int = -1
	private var menu = ArrayList<ContextMenus>()
	private var childs = ArrayList<ContextMenus.MenuItem>()

	override fun layoutRes(): Int = R.layout.simple_list_dialog

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		id = arguments!!.getString(BundleConstant.EXTRA)
		listItem = arguments!!.getParcelable(BundleConstant.EXTRA_TWO) ?: Any()
		positionItem = arguments!!.getInt(BundleConstant.EXTRA_THREE, -1)
		menu = arguments!!.getParcelableArrayList<ContextMenus>(BundleConstant.ITEM)
		childs = menu[0].items
		setTitle(menu[0].title)
		val adapter = ContextListAdapter(childs)
		adapter.listener = this
		recycler.addDivider()
		recycler.adapter = adapter
		recycler.layoutManager = LinearLayoutManager(activity)
		fastScroller.attachRecyclerView(recycler)
	}

	private fun recreate(menuForLevel: List<ContextMenus>) {
		setTitle(menuForLevel[0].title)
		childs.clear()
		childs.addAll(menuForLevel[0].items)
		recycler.adapter.notifyDataSetChanged()
	}

	private fun setTitle(titleText: String) {
		if (titleText.isNotEmpty()) {
			title.text = titleText
		} else title.visibility = View.GONE
	}

	override fun onItemClick(position: Int, v: View?, item: ContextMenus.MenuItem) {
		if (callbacks != null) {
			val menuForLevel: List<ContextMenus> = menu.filter { it.parent == item.id }
			if (menuForLevel.isEmpty() || id == item.id) {
				dismiss()
				if (PrefGetter.getLoggedUser() == null && item.logged) {
					Toasty.error(App.instance, getString(R.string.unauthorized_user), Toast.LENGTH_SHORT).show()
					return
				}
				callbacks!!.onItemSelected(id, item, if (positionItem != -1) positionItem  else position, listItem)
			} else {
				id = item.id
				recreate(menuForLevel)
			}
		}
	}

	override fun onItemLongClick(position: Int, v: View?, item: ContextMenus.MenuItem) {
	}


	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment != null && parentFragment is ListDialogViewActionCallback) {
			callbacks = parentFragment as ListDialogViewActionCallback
		} else if (context is ListDialogViewActionCallback) {
			callbacks = context
		}
	}

	override fun onDetach() {
		super.onDetach()
		callbacks = null
	}

	fun initArguments(id: String, objects: ArrayList<ContextMenus>?, item: Parcelable, position: Int) {
		arguments = Bundler.start()
				.put(BundleConstant.EXTRA, id)
				.put(BundleConstant.EXTRA_TWO, item)
				.put(BundleConstant.EXTRA_THREE, position)
				.put(BundleConstant.ITEM, objects!!)
				.end()
	}

	fun initArguments(id: String, objects: ArrayList<ContextMenus>?) {
		arguments = Bundler.start()
				.put(BundleConstant.EXTRA, id)
				.put(BundleConstant.ITEM, objects!!)
				.end()
	}

	interface ListDialogViewActionCallback {
		fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any)
	}

}
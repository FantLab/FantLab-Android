package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.ContextListAdapter
import ru.fantlab.android.ui.base.BaseBottomSheetDialog
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class ContextMenuDialogView : BaseBottomSheetDialog(), BaseViewHolder.OnItemClickListener<ContextMenus.MenuItem> {
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	private var callbacks: ListDialogViewActionCallback? = null
	private lateinit var id: String
	private lateinit var listItem: Any
	private var positionItem: Int = 0
	private var menu = ArrayList<ContextMenus>()
	private var childs = ArrayList<ContextMenus.MenuItem>()

	override fun layoutRes(): Int {
		return R.layout.simple_list_dialog
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		id = arguments!!.getString(BundleConstant.EXTRA)
		listItem = arguments!!.getParcelable(BundleConstant.EXTRA_TWO)
		positionItem = arguments!!.getInt(BundleConstant.EXTRA_THREE)
		menu = arguments!!.getParcelableArrayList<ContextMenus>(BundleConstant.ITEM)
		childs = menu[0].items
		title.text = menu[0].title
		if (title.text.isEmpty()) title.visibility = View.GONE
		val adapter = ContextListAdapter(childs)
		adapter.listener = this
		recycler.addDivider()
		recycler.adapter = adapter
		recycler.layoutManager = LinearLayoutManager(activity)
		fastScroller.attachRecyclerView(recycler)
	}

	private fun recreate(menuForLevel: List<ContextMenus>){
		title.text = menuForLevel[0].title
		childs.clear()
		childs.addAll(menuForLevel[0].items)
		recycler.adapter.notifyDataSetChanged()
	}
	override fun onItemClick(position: Int, v: View?, item: ContextMenus.MenuItem) {
		if (callbacks != null) {
			val menuForLevel: List<ContextMenus> = menu.filter { it.parent == item.id }
			if (menuForLevel.isEmpty() || id == item.id) {
				dismiss()
				callbacks!!.onItemSelected(item, listItem, positionItem)
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

	interface ListDialogViewActionCallback {
		fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int)
	}

}
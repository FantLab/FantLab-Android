package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.simple_list_dialog.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.SimpleListAdapter
import ru.fantlab.android.ui.base.BaseDialogFragment
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ListDialogView<T : Parcelable> : BaseDialogFragment<BaseMvp.View, BasePresenter<BaseMvp.View>>(), BaseViewHolder.OnItemClickListener<T> {

	private var callbacks: ListDialogViewActionCallback? = null

	init {
		suppressAnimation = true
	}

	override fun fragmentLayout(): Int {
		return R.layout.simple_list_dialog
	}

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		val objects = arguments!!.getParcelableArrayList<T>(BundleConstant.ITEM)
		if (objects.size == 1) {
			onItemClick(0, view, objects[0])
			return
		}
		val titleText = arguments!!.getString(BundleConstant.EXTRA)
		title.text = titleText
		if (objects != null) {
			val adapter = SimpleListAdapter<T>(objects)
			adapter.listener = this
			recycler.addDivider()
			recycler.adapter = adapter
			recycler.layoutManager = LinearLayoutManager(activity)
		} else {
			dismiss()
		}
		fastScroller.attachRecyclerView(recycler)
	}

	override fun providePresenter(): BasePresenter<BaseMvp.View> {
		return BasePresenter()
	}

	override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {
	}

	override fun onDialogDismissed() {
	}

	override fun onItemClick(position: Int, v: View?, item: T) {
		dismiss()
		if (callbacks != null) callbacks!!.onItemSelected(item, position)
	}

	override fun onItemLongClick(position: Int, v: View?, item: T) {
	}

	fun initArguments(title: String, objects: ArrayList<T>) {
		arguments = Bundler.start()
				.put(BundleConstant.EXTRA, title)
				.put(BundleConstant.ITEM, objects)
				.end()
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

	interface ListDialogViewActionCallback {
		fun <T> onItemSelected(item: T, position: Int)
	}
}
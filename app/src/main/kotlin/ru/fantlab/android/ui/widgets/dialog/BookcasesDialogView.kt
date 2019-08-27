package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.simple_list_dialog.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.BookcaseSelectorAdapter
import ru.fantlab.android.ui.adapter.viewholder.BookcaseSelectionViewHolder
import ru.fantlab.android.ui.base.BaseDialogFragment
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class BookcasesDialogView : BaseDialogFragment<BaseMvp.View, BasePresenter<BaseMvp.View>>(),
		BaseViewHolder.OnItemClickListener<BookcaseSelection>,
		BookcaseSelectionViewHolder.OnItemSelectListener<BookcaseSelection> {

	private var callbacks: BookcasesDialogViewActionCallback? = null
	private val adapter: BookcaseSelectorAdapter by lazy { BookcaseSelectorAdapter(arrayListOf()) }

	override fun fragmentLayout(): Int = R.layout.simple_list_dialog

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		val objects = arguments!!.getParcelableArrayList<BookcaseSelection>(BundleConstant.ITEM)
		val titleText = arguments!!.getString(BundleConstant.EXTRA)
		title.text = titleText
		if (objects != null) {
			adapter.addItems(objects)
			adapter.listener = this
			adapter.selectionListener = this
			recycler.addDivider()
			recycler.adapter = adapter
			recycler.layoutManager = LinearLayoutManager(activity)
		} else {
			dismiss()
		}
		fastScroller.attachRecyclerView(recycler)
	}

	override fun providePresenter(): BasePresenter<BaseMvp.View> = BasePresenter()

	override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {
	}

	override fun onDialogDismissed() {
	}

	override fun onItemClick(position: Int, v: View?, item: BookcaseSelection) {
		callbacks?.onBookcaseClick(item, position)
	}

	override fun onItemLongClick(position: Int, v: View?, item: BookcaseSelection) {}

	override fun onItemSelected(position: Int, v: View?, item: BookcaseSelection) {
		callbacks?.onBookcaseSelected(item, position)
	}

	fun initArguments(title: String, objects: ArrayList<BookcaseSelection>) {
		arguments = Bundler.start()
				.put(BundleConstant.EXTRA, title)
				.put(BundleConstant.ITEM, objects)
				.end()
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (parentFragment != null && parentFragment is BookcasesDialogViewActionCallback) {
			callbacks = parentFragment as BookcasesDialogViewActionCallback
		} else if (context is BookcasesDialogViewActionCallback) {
			callbacks = context
		}
	}

	override fun onDetach() {
		callbacks = null
		super.onDetach()
	}

	interface BookcasesDialogViewActionCallback {
		fun onBookcaseClick(item: BookcaseSelection, position: Int)
		fun onBookcaseSelected(item: BookcaseSelection, position: Int)
	}
}
package ru.fantlab.android.ui.modules.editor.smiles

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import butterknife.BindView
import butterknife.OnTextChanged
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Smile
import ru.fantlab.android.ui.adapter.SmileAdapter
import ru.fantlab.android.ui.base.BaseMvpBottomSheetDialogFragment
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.layoutManager.GridManager
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class SmileBottomSheet : BaseMvpBottomSheetDialogFragment<SmileMvp.View, SmilePresenter>(), SmileMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	val adapter: SmileAdapter by lazy { SmileAdapter(this) }
	private var smileCallback: SmileMvp.SmileCallback? = null

	override fun fragmentLayout(): Int = R.layout.smile_popup_layout

	override fun providePresenter(): SmilePresenter = SmilePresenter()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		recycler.adapter = adapter
		fastScroller.attachRecyclerView(recycler)
		presenter.onLoadSmile()
		val gridManager = recycler.layoutManager as GridManager
		gridManager.iconSize = resources.getDimensionPixelSize(R.dimen.header_icon_size)
	}

	@OnTextChanged(value = [(R.id.editText)], callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
	fun onTextChange(text: Editable) {
		adapter.filter.filter(text)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		smileCallback = when {
			parentFragment is SmileMvp.SmileCallback -> parentFragment as SmileMvp.SmileCallback
			context is SmileMvp.SmileCallback -> context
			else -> throw IllegalArgumentException("$context must implement SmileMvp.SmileCallback")
		}
	}

	override fun onDetach() {
		smileCallback = null
		super.onDetach()
	}

	override fun clearAdapter() {
		adapter.clear()
	}

	override fun onAddSmile(smile: Smile) {
		adapter.addItem(smile)
	}

	override fun onItemClick(position: Int, v: View?, item: Smile) {
		smileCallback?.onSmileAdded(item)
		dismiss()
	}

	override fun onItemLongClick(position: Int, v: View?, item: Smile) {}
}
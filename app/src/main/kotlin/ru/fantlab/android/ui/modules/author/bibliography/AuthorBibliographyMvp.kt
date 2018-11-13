package ru.fantlab.android.ui.modules.author.bibliography

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter

interface AuthorBibliographyMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback,
			RatingDialogView.RatingDialogViewActionCallback {

		fun onInitViews(cycles: WorksBlocks?, works: WorksBlocks?)

		fun onItemLongClicked(item: TreeNode<*>, position: Int)

		fun onSetMark(position: Int, mark: Int)

		fun onGetMarks(marks: ArrayList<MarkMini>)

	}

	interface Presenter : BaseMvp.Presenter,
			TreeViewAdapter.OnItemClickListener {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getBibliography(): WorksBlocks?

		fun getMarks(userId: Int?, workIds: ArrayList<Int?>)
	}
}
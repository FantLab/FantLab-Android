package ru.fantlab.android.ui.modules.forums

import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ForumsMvp {

	interface View : BaseMvp.View  {

		fun setTitle(title: String)

		fun openForum(page: String, id: Int, title: String, forumId: Int, isClosed: Boolean = false)

	}

	interface Presenter : BaseMvp.Presenter {

		fun openForum(fragmentManager: FragmentManager, page: String, id: Int, title: String, forumId: Int, isClosed: Boolean = false)

	}
}
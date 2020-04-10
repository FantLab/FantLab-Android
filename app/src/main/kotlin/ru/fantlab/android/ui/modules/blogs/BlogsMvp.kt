package ru.fantlab.android.ui.modules.blogs

import androidx.fragment.app.FragmentManager
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface BlogsMvp {

	interface View : BaseMvp.View  {

		fun setTitle(title: String)

		fun openBlog(blogId: Int, title: String, isClosed: Boolean)

	}

	interface Presenter : BaseMvp.Presenter {

		fun openBlog(fragmentManager: FragmentManager,blogId: Int, title: String, isClosed: Boolean)

	}
}
package ru.fantlab.android.ui.modules.blogs

import androidx.fragment.app.FragmentManager
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.blogs.articles.list.BlogsArticlesFragment

class BlogsPresenter : BasePresenter<BlogsMvp.View>(), BlogsMvp.Presenter {

	override fun openBlog(fragmentManager: FragmentManager, blogId: Int, title: String, isClosed: Boolean) {
		val blogFragment = BlogsArticlesFragment.newInstance(blogId, title, isClosed)
		val blogsView = AppHelper.getFragmentByTag(fragmentManager, BlogsArticlesFragment.TAG) as BlogsArticlesFragment?

		val currentVisible = ActivityHelper.getVisibleFragment(fragmentManager)

		if (blogsView == null) {
			AppHelper.onAddAndHide(fragmentManager, blogFragment, currentVisible)
		} else {
			AppHelper.onShowHideFragment(fragmentManager, blogsView, currentVisible)
		}

	}
}
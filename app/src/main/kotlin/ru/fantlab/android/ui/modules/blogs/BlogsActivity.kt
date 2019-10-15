package ru.fantlab.android.ui.modules.blogs

import android.os.Bundle
import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.blogs.main.BlogsMainFragment

class BlogsActivity : BaseActivity<BlogsMvp.View, BlogsPresenter>(), BlogsMvp.View {

	override fun layout(): Int = R.layout.blogs_main_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): BlogsPresenter = BlogsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		selectMenuItem(R.id.blogs, true)
		if (savedInstanceState == null) {
			val blogsFragment = BlogsMainFragment()
			supportFragmentManager
					.beginTransaction()
					.add(R.id.container, blogsFragment, BlogsMainFragment.TAG)
					.commit()
		}
	}

	override fun setTitle(title: String) {
		this.title = title
		appbar?.setExpanded(true, false)
	}

	override fun onBackPressed() {
		val fm = supportFragmentManager
		when {
			fm.backStackEntryCount > 0 -> {
				fm.popBackStack()
				appbar?.setExpanded(true, false)
			}
			else -> super.onBackPressed()
		}
	}
}
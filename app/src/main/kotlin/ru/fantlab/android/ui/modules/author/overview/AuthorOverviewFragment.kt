package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import com.google.gson.GsonBuilder
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.Author
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class AuthorOverviewFragment : BaseFragment<AuthorOverviewMvp.View, AuthorOverviewPresenter>(),
		AuthorOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var author: Author? = null

	override fun fragmentLayout() = R.layout.author_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			author = savedInstanceState.getParcelable("author")
			if (author != null) {
				onInitViews(author!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorOverviewPresenter()

	override fun onInitViews(author: Author) {
		hideProgress()
		Timber.d("author: ${GsonBuilder().setPrettyPrinting().create().toJson(author)}")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("author", author)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progress.visibility = View.GONE
	}

	override fun showErrorMessage(msgRes: String) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(authorId: Int): AuthorOverviewFragment {
			val view = AuthorOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}
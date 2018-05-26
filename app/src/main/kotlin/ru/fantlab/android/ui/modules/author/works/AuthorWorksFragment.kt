package ru.fantlab.android.ui.modules.author.works

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class AuthorWorksFragment : BaseFragment<AuthorWorksMvp.View, AuthorWorksPresenter>(),
		AuthorWorksMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var cycles: WorksBlocks? = null
	private var works: WorksBlocks? = null

	override fun fragmentLayout() = R.layout.author_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			cycles = savedInstanceState.getParcelable("cycles")
			works = savedInstanceState.getParcelable("works")
			if (cycles != null && works != null) {
				onInitViews(cycles!!, works!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorWorksPresenter()

	override fun onInitViews(cycles: WorksBlocks?, works: WorksBlocks) {
		hideProgress()
		Timber.d("cycles: $cycles")
		Timber.d("works: $works")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("cycles", cycles)
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

		fun newInstance(authorId: Int): AuthorWorksFragment {
			val view = AuthorWorksFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}
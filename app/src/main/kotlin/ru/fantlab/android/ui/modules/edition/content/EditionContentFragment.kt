package ru.fantlab.android.ui.modules.edition.content

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class EditionContentFragment : BaseFragment<EditionContentMvp.View, EditionContentPresenter>(),
		EditionContentMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var content: ArrayList<String>? = null

	override fun fragmentLayout() = R.layout.edition_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			//content = savedInstanceState.getParcelableArrayList<String>("content")
			if (content != null) {
				onInitViews(content!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = EditionContentPresenter()

	override fun onInitViews(content: ArrayList<String>) {
		hideProgress()
		Timber.d("content: $content")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		//outState.putParcelableArrayList("content", content)
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

		fun newInstance(editionId: Int): EditionContentFragment {
			val view = EditionContentFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}
}
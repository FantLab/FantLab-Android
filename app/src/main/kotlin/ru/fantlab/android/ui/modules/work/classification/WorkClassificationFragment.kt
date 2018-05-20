package ru.fantlab.android.ui.modules.work.classification

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import com.google.gson.GsonBuilder
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.GenreGroup
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class WorkClassificationFragment : BaseFragment<WorkClassificationMvp.View, WorkClassificationPresenter>(),
		WorkClassificationMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var workClassification: ArrayList<GenreGroup>? = null

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			workClassification = savedInstanceState.getParcelableArrayList("workClassification")
			if (workClassification != null) {
				onInitViews(workClassification!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkClassificationPresenter()

	override fun onInitViews(classificatory: ArrayList<GenreGroup>) {
		hideProgress()
		Timber.d("classificatory: ${GsonBuilder().setPrettyPrinting().create().toJson(classificatory)}")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("workClassification", workClassification)
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

		fun newInstance(workId: Int): WorkClassificationFragment {
			val view = WorkClassificationFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}
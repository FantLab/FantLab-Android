package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class WorkEditionsFragment : BaseFragment<WorkEditionsMvp.View, WorkEditionsPresenter>(),
		WorkEditionsMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var workEditions: EditionsBlocks? = null
	private var workEditionsInfo: EditionsInfo? = null

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			workEditions = savedInstanceState.getParcelable("workEditions")
			workEditionsInfo = savedInstanceState.getParcelable("workEditionsInfo")
			if (workEditions != null && workEditionsInfo != null) {
				onInitViews(workEditions, workEditionsInfo!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkEditionsPresenter()

	override fun onInitViews(editions: EditionsBlocks?, editionsInfo: EditionsInfo) {
		hideProgress()
		Timber.d("editions: $editions")
		Timber.d("editionsInfo: $editionsInfo")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("workEditions", workEditions)
		outState.putParcelable("workEditionsInfo", workEditionsInfo)
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

		fun newInstance(workId: Int): WorkEditionsFragment {
			val view = WorkEditionsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}
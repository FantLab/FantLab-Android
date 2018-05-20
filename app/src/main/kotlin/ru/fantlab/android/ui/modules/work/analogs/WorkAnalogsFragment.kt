package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import com.google.gson.GsonBuilder
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.WorkAnalog
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class WorkAnalogsFragment : BaseFragment<WorkAnalogsMvp.View, WorkAnalogsPresenter>(),
		WorkAnalogsMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var workAnalogs: ArrayList<WorkAnalog>? = null

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			workAnalogs = savedInstanceState.getParcelableArrayList("workAnalogs")
			if (workAnalogs != null) {
				onInitViews(workAnalogs!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkAnalogsPresenter()

	override fun onInitViews(analogs: ArrayList<WorkAnalog>) {
		hideProgress()
		Timber.d("analogs: ${GsonBuilder().setPrettyPrinting().create().toJson(analogs)}")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelableArrayList("workAnalogs", workAnalogs)
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

		fun newInstance(workId: Int): WorkAnalogsFragment {
			val view = WorkAnalogsFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}
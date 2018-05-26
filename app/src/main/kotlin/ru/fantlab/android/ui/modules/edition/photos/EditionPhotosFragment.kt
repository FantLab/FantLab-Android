package ru.fantlab.android.ui.modules.edition.photos

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import timber.log.Timber

class EditionPhotosFragment : BaseFragment<EditionPhotosMvp.View, EditionPhotosPresenter>(),
		EditionPhotosMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	private var editionPhotos: AdditionalImages? = null

	override fun fragmentLayout() = R.layout.edition_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			editionPhotos = savedInstanceState.getParcelable("editionPhotos")
			if (editionPhotos != null) {
				onInitViews(editionPhotos!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = EditionPhotosPresenter()

	override fun onInitViews(additionalImages: AdditionalImages?) {
		hideProgress()
		Timber.d("photos: $additionalImages")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("editionPhotos", editionPhotos)
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

		fun newInstance(editionId: Int): EditionPhotosFragment {
			val view = EditionPhotosFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}
}
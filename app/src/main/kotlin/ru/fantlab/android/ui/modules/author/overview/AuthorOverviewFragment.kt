package ru.fantlab.android.ui.modules.author.overview

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import android.view.View
import kotlinx.android.synthetic.main.author_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.model.Biography
import ru.fantlab.android.data.dao.model.Classification
import ru.fantlab.android.data.dao.model.GenreGroup
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.adapter.ClassificationAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp

class AuthorOverviewFragment : BaseFragment<AuthorOverviewMvp.View, AuthorOverviewPresenter>(),
		AuthorOverviewMvp.View {

	private var pagerCallback: AuthorPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.author_overview_layout

	private val adapterClassification: ClassificationAdapter by lazy { ClassificationAdapter(arrayListOf()) }

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun providePresenter() = AuthorOverviewPresenter()

	override fun onInitViews(author: Author, biography: Biography?, classificatory: ArrayList<GenreGroup>) {
		hideProgress()
		coverLayouts.setUrl("https:${author.image}")

		if (!InputHelper.isEmpty(author.countryName)) {
			authorCountryInfo.text = author.countryName
			authorCountryInfoBlock.visibility = View.VISIBLE
		} else authorCountryInfoBlock.visibility = View.GONE

		if (InputHelper.isEmpty(author.name)) {
			authorName.text = author.nameOriginal
			authorNameOrig.visibility = View.GONE
		} else {
			authorName.text = author.name
			if (!InputHelper.isEmpty(author.nameOriginal))
				authorNameOrig.text = author.nameOriginal
			else
				authorNameOrig.visibility = View.GONE
		}

		pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(author.name)) author.name else author.nameOriginal)

		if (!author.birthDay.isNullOrEmpty()) {
			editionBorn.text = author.birthDay
			editionBornBlock.visibility = View.VISIBLE
		} else editionBornBlock.visibility = View.GONE

		if (!author.deathDay.isNullOrEmpty()) {
			editionDie.text = author.deathDay
			editionDieBlock.visibility = View.VISIBLE
		} else editionDieBlock.visibility = View.GONE


		if (biography?.sites != null && biography.sites.isNotEmpty()) {
			val sb = StringBuilder()
			biography.sites.forEachIndexed { index, it ->
				sb.append("<a href=\"${it.site}\">${it.description.capitalize()}</a>")
				if (index <= biography.sites.size) sb.append("\n")
			}
			authorSite.html = sb.toString()
		}

		if (author.isOpened == 0) {
			notOpened.visibility = View.VISIBLE
		} else notOpened.visibility = View.GONE

		if (!biography?.biography.isNullOrEmpty()) {
			val source = when {
				biography!!.source.isNotEmpty() && biography.sourceLink.isNotEmpty() -> {
					val sourceText = "© <a href=\"${biography.sourceLink}\">${biography.source}</a>"
					sourceText
				}
				biography.source.isNotEmpty() -> {
					getString(R.string.copyright, biography.source)
				}
				biography.sourceLink.isNotEmpty() -> {
					getString(R.string.copyright, biography.sourceLink)
				}
				else -> {
					""
				}
			}

			biographyText.html = biography.biography.replace("(\r\n)+".toRegex(), "\n").trim() + "\n\n" + source

			biographyBlock.visibility = View.VISIBLE
		} else biographyBlock.visibility = View.GONE

		onSetClassification(classificatory)
	}

	private fun onSetClassification(classificatory: ArrayList<GenreGroup>) {

		if (classificatory.isEmpty()) {
			classificationBLock.visibility = View.GONE
			return
		}

		val arrayOfClass = arrayListOf<Classification>()

		classificatory.forEachIndexed { index, item ->
			val title = item.label
			val groups = arrayListOf<String>()
			item.genres.forEachIndexed { subIndex, pair ->
				val currentTitle = pair.second.label
				groups.add(currentTitle)
			}
			arrayOfClass.add(Classification(title, groups))
		}

		adapterClassification.insertItems(arrayOfClass)
		classificationList.adapter = adapterClassification
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progress.visibility = View.GONE
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun onShowErrorView(msgRes: String?) {
		parentView.visibility = View.GONE
		stateLayout.setEmptyText(R.string.network_error)
		stateLayout.showErrorState()
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is AuthorPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	companion object {

		fun newInstance(authorId: Int): AuthorOverviewFragment {
			val view = AuthorOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}
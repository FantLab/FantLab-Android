package ru.fantlab.android.ui.modules.author.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.author_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.model.Biography
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView

class AuthorOverviewFragment : BaseFragment<AuthorOverviewMvp.View, AuthorOverviewPresenter>(),
		AuthorOverviewMvp.View {

	private var pagerCallback: AuthorPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.author_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun providePresenter() = AuthorOverviewPresenter()

	override fun onInitViews(author: Author, biography: Biography?) {
		hideProgress()
		coverLayout.setUrl("https:${author.image}")

		Glide.with(context)
				.load("https://${LinkParserHelper.HOST_DEFAULT}/img/flags/${author.countryId}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(langIcon)

		if (!InputHelper.isEmpty(author.countryName))
			country.text = author.countryName
		else
			country.visibility = View.GONE

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

		if (author.deathDay != null) {
			if (author.birthDay != null)
				date.text = String.format("%s ‒ %s", author.birthDay, author.deathDay)
		} else if (author.birthDay != null) {
			date.text = author.birthDay
		} else {
			date.visibility = View.GONE
		}

		if (biography?.sites != null && biography.sites.isNotEmpty()) {
			val sb = StringBuilder()
			biography.sites.forEachIndexed { index, it ->
				sb.append("<a href=\"${it.site}\">${it.description.capitalize()}</a>")
				if (index <= biography.sites.size) sb.append("\n")
			}
			homepage.html = sb.toString()
		}

		if (author.isOpened == 1) {
			notOpened.visibility = View.GONE
		}

		val bio = biography?.biography
				?.replace("(\r\n)+".toRegex(), "\n")
				?.trim()

		if (!bio.isNullOrEmpty()) {
			biographyText.html = bio
			when {
				biography!!.source.isNotEmpty() && biography.sourceLink.isNotEmpty() -> {
					val sourceText = "© <a href=\"${biography.sourceLink}\">${biography.source}</a>"
					source.text = Html.fromHtml(sourceText)
				}
				biography.source.isNotEmpty() -> {
					source.text = getString(R.string.copyright, biography.source)
				}
				biography.sourceLink.isNotEmpty() -> {
					source.text = getString(R.string.copyright, biography.sourceLink)
				}
				else -> {
					source.visibility = View.GONE
				}
			}
		} else {
			biographyCard.visibility = View.GONE
		}
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
		stateLayout.showEmptyState()
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onAttach(context: Context?) {
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
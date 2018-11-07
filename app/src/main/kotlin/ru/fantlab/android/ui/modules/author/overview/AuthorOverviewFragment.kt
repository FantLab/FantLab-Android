package ru.fantlab.android.ui.modules.author.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.text.Html
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.model.Biography
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView

class AuthorOverviewFragment : BaseFragment<AuthorOverviewMvp.View, AuthorOverviewPresenter>(),
		AuthorOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
    @BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
    @BindView(R.id.author) lateinit var authorName: FontTextView
    @BindView(R.id.date) lateinit var date: FontTextView
    @BindView(R.id.country) lateinit var country: FontTextView
	@BindView(R.id.notOpened) lateinit var notOpened: FontTextView
	@BindView(R.id.biographyCard) lateinit var biographyCard: View
    @BindView(R.id.biography) lateinit var biographyText: FontTextView
    @BindView(R.id.source) lateinit var source: FontTextView

	private var author: Author? = null
	private var biography: Biography? = null
	private var pagerCallback: AuthorPagerMvp.View? = null
	override fun fragmentLayout() = R.layout.author_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			author = savedInstanceState.getParcelable("author")
            biography = savedInstanceState.getParcelable("biography")
			if (author != null && biography != null ) {
				onInitViews(author!!, biography)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorOverviewPresenter()

	override fun onInitViews(author: Author, biography: Biography?) {
		hideProgress()
		coverLayout.setUrl("https:${author.image}")

		if (!InputHelper.isEmpty(author.countryName))
			country.text = author.countryName
		else
			country.visibility = View.GONE

        authorName.text = if (author.name.isNotEmpty()) {
            if (author.nameOriginal.isNotEmpty()) {
                "${author.name} / ${author.nameOriginal}"
            } else {
                author.name
            }
        } else {
            author.nameOriginal
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

		if (author.isOpened == 1) {
			notOpened.visibility = View.GONE
		}

		val bio = biography?.biography
				?.replace("(\r\n)+".toRegex(), "\n")
				?.replace("\\[(.*?)]".toRegex(), "") // удаление только тегов, без содержимого (!)
				?.replace("<(.*?)>".toRegex(), "") // аналогично, но с другими скобками
				?.trim()

		if (!bio.isNullOrEmpty()) {
			biographyText.text = bio

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

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("author", author)
		outState.putParcelable("biography", biography)
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

	override fun onSetTitle(title: String) {
		pagerCallback?.onSetTitle(title)
	}

	companion object {

		fun newInstance(authorId: Int): AuthorOverviewFragment {
			val view = AuthorOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}
package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import com.google.gson.GsonBuilder
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.model.Biography
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import timber.log.Timber

class AuthorOverviewFragment : BaseFragment<AuthorOverviewMvp.View, AuthorOverviewPresenter>(),
		AuthorOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
    @JvmField @BindView(R.id.coverLayout) var coverLayout: CoverLayout? = null
    @BindView(R.id.author) lateinit var authorName: FontTextView
    @BindView(R.id.date) lateinit var date: FontTextView
    @BindView(R.id.country) lateinit var country: FontTextView
    @BindView(R.id.description) lateinit var description: FontTextView
    @BindView(R.id.source) lateinit var sourceDescription: FontTextView

	private var author: Author? = null
	private var biography: Biography? = null
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
		Timber.d("author: ${GsonBuilder().setPrettyPrinting().create().toJson(author)}")
        coverLayout?.setUrl("https:${author.image}")

        authorName.text = if (author.name.isNotEmpty()) {
            if (author.nameOriginal.isNotEmpty()) {
                "${author.name} / ${author.nameOriginal}"
            } else {
                author.name
            }
        } else {
            author.nameOriginal
        }

        if (author.deathDay != null) {
            if (author.birthDay != null)
                date.text = String.format("%s ‒ %s", author.birthDay, author.deathDay)
        } else if (author.birthDay != null)
            date.text = author.birthDay

        country.text = author.countryName
        description.text = biography?.biography
                ?.replace("(\r\n)+".toRegex(), "\n")
                ?.replace("\\[(.*?)]".toRegex(), "") // удаление только тегов, без содержимого (!)
                ?.replace("<(.*?)>".toRegex(), "") // аналогично, но с другими скобками

        if (biography?.sourceLink?.isNotEmpty()!!)
            sourceDescription.text = String.format("%s: %s", getString(R.string.source), biography.sourceLink)
        else
            sourceDescription.text = getString(R.string.no_source)
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

	companion object {

		fun newInstance(authorId: Int): AuthorOverviewFragment {
			val view = AuthorOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}
}
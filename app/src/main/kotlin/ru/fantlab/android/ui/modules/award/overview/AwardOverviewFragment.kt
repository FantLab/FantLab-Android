package ru.fantlab.android.ui.modules.award.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.markdown.MarkDownProvider
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.award.AwardPagerMvp
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.ForegroundImageView

class AwardOverviewFragment : BaseFragment<AwardOverviewMvp.View, AwardOverviewPresenter>(),
		AwardOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
    @BindView(R.id.coverLayout) lateinit var coverLayout: ForegroundImageView
    @BindView(R.id.langIcon) lateinit var langLayout: ImageView
    @BindView(R.id.title) lateinit var title: FontTextView
    @BindView(R.id.title2) lateinit var title2: FontTextView
    @BindView(R.id.description) lateinit var description: FontTextView
    @BindView(R.id.comment) lateinit var comment: FontTextView
    @BindView(R.id.notes) lateinit var notes: FontTextView
    @BindView(R.id.country) lateinit var country: FontTextView
    @BindView(R.id.date) lateinit var date: FontTextView
    @BindView(R.id.homepage) lateinit var homepage: FontTextView

	private var award: Award? = null
	override fun fragmentLayout() = R.layout.award_overview_layout
	private var pagerCallback: AwardPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			award = savedInstanceState.getParcelable("award")
			if (award != null) {
				onInitViews(award!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AwardOverviewPresenter()

	override fun onInitViews(award: Award) {
		hideProgress()
		if (award.isOpened == 0) {
			showErrorMessage(getString(R.string.award_not_opened))
			activity?.finish()
			return
		}
		Glide.with(context)
				.load("https://${LinkParserHelper.HOST_DATA}/images/awards/${award.awardId}")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(coverLayout)

		if (award.rusname.isBlank()) {
			title.text = award.name
			title2.visibility = View.GONE
		} else {
			title.text = award.rusname
			if (award.name.isBlank()) {
				title2.visibility = View.GONE
			} else title2.text = award.name
		}

				MarkDownProvider.setMdText(description, award.description
				.replace("[*]", "<li>")
				.replace("\\u003d".toRegex(), "=")
				.replace("\\[URL=(.*?)](.*?)\\[/URL]".toRegex(), "<a href=\$1>\$2</a>")
				.replace("\\[(?!autor|/autor)(?!award|/award)(?!link|/link)(.*?)]".toRegex(), "<$1>")
				.replace("(<PHOTO.*?>)".toRegex(), "")
		)

		MarkDownProvider.setMdText(comment, award.comment
				.replace("[*]", "<li>")
				.replace("\\u003d".toRegex(), "=")
				.replace("\\[URL=(.*?)](.*?)\\[/URL]".toRegex(), "<a href=\$1>\$2</a>")
				.replace("\\[(?!autor|/autor)(?!award|/award)(?!link|/link)(.*?)]".toRegex(), "<$1>")
				.replace("(<PHOTO.*?>)".toRegex(), ""))

		MarkDownProvider.setMdText(notes, award.notes
				.replace("[*]", "<li>")
				.replace("\\u003d".toRegex(), "=")
				.replace("\\[URL=(.*?)](.*?)\\[/URL]".toRegex(), "<a href=\$1>\$2</a>")
				.replace("\\[(?!autor|/autor)(?!award|/award)(?!link|/link)(.*?)]".toRegex(), "<$1>")
				.replace("(<PHOTO.*?>)".toRegex(), ""))

		Glide.with(context)
				.load("https://${LinkParserHelper.HOST_DEFAULT}/img/flags/${award.countryId}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(langLayout)

		country.text = award.countryName
		date.text = StringBuilder()
				.append(award.minDate.split("-")[0])
				.append(" - ")
				.append(award.maxDate.split("-")[0])
		homepage.text = award.homepage
    }

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("award", award)
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
		if (context is AwardPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	companion object {

		fun newInstance(awardId: Int): AwardOverviewFragment {
			val view = AwardOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, awardId).end()
			return view
		}
	}
}
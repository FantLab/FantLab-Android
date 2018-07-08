package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.CardView
import android.text.SpannableStringBuilder
import android.view.View
import butterknife.BindView
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.edition_overview_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.data.dao.model.SliderModel
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.GallerySlider

class EditionOverviewFragment : BaseFragment<EditionOverviewMvp.View, EditionOverviewPresenter>(),
		EditionOverviewMvp.View {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.compilers) lateinit var compilers: FontTextView
	@BindView(R.id.types) lateinit var types: FontTextView
	@BindView(R.id.publishers) lateinit var publishers: FontTextView
	@BindView(R.id.series) lateinit var series: FontTextView
	@BindView(R.id.copyCount) lateinit var copyCount: FontTextView
	@BindView(R.id.isbns) lateinit var isbns: FontTextView
	@BindView(R.id.coverType) lateinit var coverType: FontTextView
	@BindView(R.id.pagesCount) lateinit var pagesCount: FontTextView
	@BindView(R.id.descriptionCard) lateinit var descriptionCard: CardView
	@BindView(R.id.description) lateinit var description: FontTextView
	@BindView(R.id.notesCard) lateinit var notesCard: CardView
	@BindView(R.id.notes) lateinit var notes: FontTextView
	@BindView(R.id.progress) lateinit var progress: View
	private var edition: Edition? = null
	@State var additionalImages: AdditionalImages? = null

	override fun fragmentLayout() = R.layout.edition_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			edition = savedInstanceState.getParcelable("edition")
			if (edition != null) {
				onInitViews(edition!!, additionalImages)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = EditionOverviewPresenter()

	override fun onInitViews(edition: Edition, additionalImages: AdditionalImages?) {
		hideProgress()
		this.edition = edition
		this.additionalImages = additionalImages
		coverLayout.setUrl("https:${edition.image}")
		coverLayout.setOnClickListener {
			val slideImages = arrayListOf<SliderModel>()
			additionalImages?.cover?.map { cover ->
				cover.spine?.let { spine ->
					slideImages.add(SliderModel("https:$spine", "Корешок"))
				}
				slideImages.add(SliderModel("https:${cover.image}", cover.text))
			}
			additionalImages?.plus?.map { image ->
				slideImages.add(SliderModel("https:${image.image}", image.text))
			}
			if (slideImages.isNotEmpty()) {
				GallerySlider(context).showSlider(slideImages, 0)
			}
		}
		var sb = SpannableStringBuilder()
		var prefix: String
		val authorsList = edition.creators.authors
		if (authorsList != null && authorsList.isNotEmpty()) {
			authorsList.map { sb.append(it.name).append(", ") }
			authors.text = sb.substring(0, sb.lastIndex - 1)
		} else {
			authors.visibility = View.GONE
		}
		title.text = edition.name
		val compilersList = edition.creators.compilers
		if (compilersList != null && compilersList.isNotEmpty()) {
			sb = SpannableStringBuilder()
			prefix = if (compilersList.size > 1) "Составители: " else "Составитель: "
			sb.append(prefix)
			compilersList.map { sb.append(it.name).append(", ") }
			compilers.text = sb.substring(0, sb.lastIndex - 1)
		} else {
			compilers.visibility = View.GONE
		}
		sb = SpannableStringBuilder()
		sb.append(edition.type)
				.append(", ")
		if (edition.additionalTypes.isNotEmpty()) {
			edition.additionalTypes.map { sb.append(it).append(", ") }
		}
		types.text = sb.substring(0, sb.lastIndex - 1)
		val publishersList = edition.creators.publishers
		if (publishersList != null && publishersList.isNotEmpty()) {
			sb = SpannableStringBuilder()
			publishersList.map { sb.append(it.name).append(", ") }
			sb.append(edition.year.toString())
			publishers.text = sb
		} else {
			publishers.visibility = View.GONE
		}
		if (edition.series.isNotEmpty()) {
			sb = SpannableStringBuilder()
			prefix = if (edition.series.size > 1) "Cерии: " else "Cерия: "
			sb.append(prefix)
			edition.series.map { sb.append(it.name).append(", ") }
			series.text = sb.substring(0, sb.lastIndex - 1)
		} else {
			series.visibility = View.GONE
		}
		if (edition.copyCount != 0) {
			sb = SpannableStringBuilder()
			sb.append("Тираж: ")
					.append(edition.copyCount.toString())
					.append(" экз.")
			copyCount.text = sb
		} else {
			copyCount.visibility = View.GONE
		}
		if (edition.isbns.isNotEmpty()) {
			sb = SpannableStringBuilder()
			sb.append("ISBN: ")
			edition.isbns.map { sb.append(it).append(", ") }
			isbns.text = sb.substring(0, sb.lastIndex - 1)
		} else {
			isbns.visibility = View.GONE
		}
		sb = SpannableStringBuilder()
		sb.append("Тип обложки: ")
				.append(edition.coverType)
		coverType.text = sb
		if (edition.format != "0" && edition.formatMm != null) {
			sb = SpannableStringBuilder()
			sb.append("Формат: ")
					.append(edition.format)
					.append(" (")
					.append(edition.formatMm)
					.append(" мм")
					.append(")")
			format.text = sb
		} else {
			format.visibility = View.GONE
		}
		sb = SpannableStringBuilder()
		sb.append("Страниц: ")
				.append(edition.pages.toString())
		pagesCount.text = sb
		if (edition.description.isNotBlank()) {
			description.setHtml(edition.description)
		} else {
			descriptionCard.visibility = View.GONE
		}
		if (edition.notes.isNotBlank()) {
			notes.setHtml(edition.notes)
		} else {
			notesCard.visibility = View.GONE
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("edition", edition)
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

		fun newInstance(editionId: Int): EditionOverviewFragment {
			val view = EditionOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}
}
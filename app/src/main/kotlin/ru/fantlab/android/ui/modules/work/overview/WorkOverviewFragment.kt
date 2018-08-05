package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.markdown.MarkDownProvider
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.ui.adapter.AwardsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.work.analogs.WorkAnalogsFragment
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.dialog.ListDialogView
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView

class WorkOverviewFragment : BaseFragment<WorkOverviewMvp.View, WorkOverviewPresenter>(),
		WorkOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
    @BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
    @BindView(R.id.authorLayout) lateinit var authorLayout: AvatarLayout
    @BindView(R.id.author) lateinit var author: FontTextView
    @BindView(R.id.author2) lateinit var author2: FontTextView
    @BindView(R.id.title) lateinit var name: FontTextView
    @BindView(R.id.title2) lateinit var name2: FontTextView
    @BindView(R.id.rate) lateinit var rate: FontTextView
    @BindView(R.id.types) lateinit var types: FontTextView
    @BindView(R.id.description) lateinit var description: FontTextView
    @BindView(R.id.notes) lateinit var notes: FontTextView
    @BindView(R.id.recyclerNoms) lateinit var nomsList: DynamicRecyclerView
    @BindView(R.id.recyclerWins) lateinit var winsList: DynamicRecyclerView

    private var work: Work? = null

	private val adapterNoms: AwardsAdapter by lazy { AwardsAdapter(presenter.getNoms()) }
	private val adapterWins: AwardsAdapter by lazy { AwardsAdapter(presenter.getWins()) }

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			work = savedInstanceState.getParcelable("work")
			if (work != null) {
				onInitViews(work!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = WorkOverviewPresenter()

	override fun onInitViews(work: Work) {
		this.work = work
		hideProgress()
		coverLayout.setUrl("https:${work.image}")
		name.text = work.name
		if (work.nameOrig.isBlank()) {
			name2.visibility = View.GONE
		} else {
			name2.text = work.nameOrig
		}
		types.text = if (work.year != null) "${work.type}, ${work.year}" else work.type
		rate.text = StringBuilder()
				.append(work.rating.rating)
				.append(" - ")
				.append(work.rating.votersCount)

		if (work.notes.isNotEmpty()) MarkDownProvider.setMdText(notes, work.notes) else getString(R.string.no_notes)

		if (!InputHelper.isEmpty(work.description)) work.description?.let { MarkDownProvider.setMdText(description, work.description) } else getString(R.string.no_description)

		authorLayout.setUrl("https://$HOST_DATA/images/autors/${work.authors[0].id}")
		author.text = work.authors[0].name
		author2.text = work.authors[0].nameOrig
		author.setOnClickListener(this)

		nomsList.adapter = adapterNoms
		winsList.adapter = adapterWins

		childFragmentManager
				.beginTransaction()
				.add(R.id.similarContainer, WorkAnalogsFragment.newInstance(work.id), WorkAnalogsFragment.TAG)
				.commit()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("work", work)
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

		fun newInstance(workId: Int): WorkOverviewFragment {
			val view = WorkOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.author -> {
				val dialogView:ListDialogView<Work.Author> = ListDialogView()
				dialogView.initArguments(getString(R.string.authors), work?.authors)
				dialogView.show(childFragmentManager, "ListDialogView")
			}
		}
	}

	override fun <T> onItemSelected(item: T, position: Int) {
		AuthorPagerActivity.startActivity(context!!, (item as Work.Author).id, item.name, 0)
	}

}
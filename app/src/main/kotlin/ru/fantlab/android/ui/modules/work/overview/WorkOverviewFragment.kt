package ru.fantlab.android.ui.modules.work.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.CardView
import android.view.View
import butterknife.BindView
import kotlinx.android.synthetic.main.work_overview_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.markdown.MarkDownProvider
import ru.fantlab.android.ui.adapter.WorkAuthorsAdapter
import ru.fantlab.android.ui.adapter.WorkAwardsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.modules.work.analogs.WorkAnalogsFragment
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView

class WorkOverviewFragment : BaseFragment<WorkOverviewMvp.View, WorkOverviewPresenter>(),
		WorkOverviewMvp.View {

	@BindView(R.id.progress) lateinit var progress: View
	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authorView) lateinit var authorView: CardView
	@BindView(R.id.title) lateinit var name: FontTextView
	@BindView(R.id.title2) lateinit var name2: FontTextView
	@BindView(R.id.rate) lateinit var rate: FontTextView
	@BindView(R.id.types) lateinit var types: FontTextView
	@BindView(R.id.description) lateinit var description: FontTextView
	@BindView(R.id.notes) lateinit var notes: FontTextView
	@BindView(R.id.recyclerNoms) lateinit var nomsList: DynamicRecyclerView
	@BindView(R.id.recyclerWins) lateinit var winsList: DynamicRecyclerView
	@BindView(R.id.recyclerAuthors) lateinit var authorsList: DynamicRecyclerView
	@BindView(R.id.aboutView) lateinit var aboutView: CardView
	@BindView(R.id.winsView) lateinit var winsView: CardView
	@BindView(R.id.nomsView) lateinit var nomsView: CardView
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout

	private lateinit var work: Work
	private val adapterNoms: WorkAwardsAdapter by lazy { WorkAwardsAdapter(arrayListOf()) }
	private val adapterWins: WorkAwardsAdapter by lazy { WorkAwardsAdapter(arrayListOf()) }
	private val adapterAuthors: WorkAuthorsAdapter by lazy { WorkAuthorsAdapter(arrayListOf()) }
	private var pagerCallback: WorkPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun providePresenter() = WorkOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(
			work: Work,
			nominations: ArrayList<Nomination>,
			wins: ArrayList<Nomination>,
			authors: ArrayList<Work.Author>
	) {
		this.work = work
		pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(work.name)) work.name else work.nameOrig)

		if (isLoggedIn()) {
			presenter.getMarks(PrefGetter.getLoggedUser()?.id ?: -1, arrayListOf(work.id))
		} else hideProgress()

		coverLayout.setUrl(if (work.image != null) "https:${work.image}" else null, R.drawable.not_found_poster)

		if (InputHelper.isEmpty(work.name)) {
			name.text = work.nameOrig
			name2.visibility = View.GONE
		} else {
			name.text = work.name
			if (!InputHelper.isEmpty(work.nameOrig))
				name2.text = work.nameOrig
			else
				name2.visibility = View.GONE
		}

		types.text = if (work.year != null) "${work.type}, ${work.year}" else work.type

		if (work.rating.votersCount != "0") {
			rate.text = StringBuilder()
					.append(work.rating.rating)
					.append(" - ")
					.append(work.rating.votersCount)
		} else rate.visibility = View.GONE

		if (!InputHelper.isEmpty(work.notes))
			work.notes.let { MarkDownProvider.setMdText(notes, work.notes) } else notes.visibility = View.GONE

		if (!InputHelper.isEmpty(work.description))
			work.description?.let { MarkDownProvider.setMdText(description, work.description) }
		else
			aboutView.visibility = View.GONE

		if (authors.isNotEmpty()) {
			adapterAuthors.insertItems(authors)
			authorsList.adapter = adapterAuthors
		} else authorView.visibility = View.GONE

		if (nominations.isNotEmpty()) {
			adapterNoms.insertItems(nominations)
			nomsList.adapter = adapterNoms
			adapterNoms.listener = presenter
		} else nomsView.visibility = View.GONE

		if (wins.isNotEmpty()) {
			adapterWins.insertItems(wins)
			winsList.adapter = adapterWins
			adapterWins.listener = presenter
		} else winsView.visibility = View.GONE

		val fs = WorkAnalogsFragment.newInstance(work.id)
		childFragmentManager
				.beginTransaction()
				.add(R.id.similarContainer, fs, WorkAnalogsFragment.TAG)
				.hide(fs)
				.commit()
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
		pagerCallback?.onError()
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

	fun showMarkDialog() {
		val author = adapterAuthors.getItem(0).name
		RatingDialogView.newInstance(10, pagerCallback?.onGetMark()?.toFloat() ?: 0f,
				work,
				"$author - ${name.text}",
				-1
		).show(childFragmentManager, RatingDialogView.TAG)
	}

	override fun <T> onItemSelected(item: T, position: Int) {
		AuthorPagerActivity.startActivity(context!!, (item as Work.Author).id, item.name, 0)
	}

	override fun onItemClicked(item: Nomination) {
		val name = if (item.awardRusName.isNotEmpty()) {
			if (item.awardName.isNotEmpty()) {
				String.format("%s / %s", item.awardRusName, item.awardName)
			} else {
				item.awardRusName
			}
		} else {
			item.awardName
		}
		AwardPagerActivity.startActivity(context!!, item.awardId, name, 1, work.id)
	}

	override fun onGetMarks(marks: ArrayList<MarkMini>) {
		hideProgress()
		pagerCallback?.onSetMarked(!marks.isEmpty(), if (!marks.isEmpty()) marks[0].mark else 0)
	}

	override fun onSetMark(mark: Int, markCount: Double, midMark: Double) {
		hideProgress()
		rate.text = StringBuilder()
				.append(midMark)
				.append(" - ")
				.append(markCount.toInt())
		pagerCallback?.onSetMarked(mark > 0, mark)
	}

	override fun onRated(rating: Float, listItem: Any, position: Int) {
		presenter.onSendMark((listItem as Work).id, rating.toInt())
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
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
}
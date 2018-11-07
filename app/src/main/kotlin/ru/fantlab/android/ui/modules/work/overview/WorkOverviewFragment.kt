package ru.fantlab.android.ui.modules.work.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.CardView
import android.view.View
import butterknife.BindView
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
    @BindView(R.id.mymark) lateinit var mymark: FontTextView
    @BindView(R.id.notes) lateinit var notes: FontTextView
    @BindView(R.id.recyclerNoms) lateinit var nomsList: DynamicRecyclerView
    @BindView(R.id.recyclerWins) lateinit var winsList: DynamicRecyclerView
    @BindView(R.id.recyclerAuthors) lateinit var authorsList: DynamicRecyclerView

    @BindView(R.id.aboutView) lateinit var aboutView: CardView
    @BindView(R.id.winsView) lateinit var winsView: CardView
    @BindView(R.id.nomsView) lateinit var nomsView: CardView

    private var work: Work? = null

	private val adapterNoms: WorkAwardsAdapter by lazy { WorkAwardsAdapter(presenter.getNoms()) }
	private val adapterWins: WorkAwardsAdapter by lazy { WorkAwardsAdapter(presenter.getWins()) }
	private val adapterAuthors: WorkAuthorsAdapter by lazy { WorkAuthorsAdapter(presenter.getAuthors()) }

	override fun fragmentLayout() = R.layout.work_overview_layout

	private var pagerCallback: WorkPagerMvp.View? = null

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
		pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(work.name)) work.name else work.nameOrig)

		if (isLoggedIn()) {
			presenter.getMarks(PrefGetter.getLoggedUser()?.id, arrayListOf(work.id))
		} else hideProgress()

		coverLayout.setUrl(if (work.image != null) "https:${work.image}" else null, R.drawable.not_found_poster)

		if (InputHelper.isEmpty(work.name)){
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

		if (adapterAuthors.itemCount > 0) {
			authorsList.adapter = adapterAuthors
		} else authorView.visibility = View.GONE

		if (adapterNoms.itemCount > 0) {
			nomsList.adapter = adapterNoms
			adapterNoms.listener = presenter
		} else nomsView.visibility = View.GONE

		if (adapterWins.itemCount > 0) {
			winsList.adapter = adapterWins
			adapterWins.listener = presenter
		} else winsView.visibility = View.GONE

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
			R.id.mymark -> {
				val author = adapterAuthors.getItem(0).name
				RatingDialogView.newInstance(10, mymark.text.toString().toFloatOrNull() ?: 0f,
						work!!,
						"${author} - ${name.text}",
						-1
				).show(childFragmentManager, RatingDialogView.TAG)
			}
		}
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
		AwardPagerActivity.startActivity(context!!, item.awardId, name, 1, work?.id ?: -1)
	}

	override fun onGetMarks(marks: ArrayList<MarkMini>) {
		hideProgress()
		if (marks.size > 0){
			mymark.text = marks[0].mark.toString()
			mymark.visibility = View.VISIBLE
		} else {
			mymark.text = getString(R.string.set_mark)
			mymark.visibility = View.VISIBLE
		}
		mymark.setOnClickListener(this)
	}

	override fun onSetMark(mark: Int) {
		hideProgress()
		if (mark == 0){
			mymark.visibility = View.GONE
		} else {
			mymark.text = mark.toString()
			mymark.visibility = View.VISIBLE
		}
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
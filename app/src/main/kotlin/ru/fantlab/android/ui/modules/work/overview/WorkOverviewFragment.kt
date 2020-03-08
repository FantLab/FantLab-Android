package ru.fantlab.android.ui.modules.work.overview

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.state_layout.*
import kotlinx.android.synthetic.main.work_overview_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.adapter.ClassificationAdapter
import ru.fantlab.android.ui.adapter.WorkAuthorsAdapter
import ru.fantlab.android.ui.adapter.ItemAwardsAdapter
import ru.fantlab.android.ui.adapter.WorkEditionsAdapter
import ru.fantlab.android.ui.adapter.viewholder.CycleContentChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.CycleContentParentViewHolder
import ru.fantlab.android.ui.adapter.viewholder.WorkTranslationHeaderViewHolder
import ru.fantlab.android.ui.adapter.viewholder.WorkTranslationViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.award.AwardPagerActivity
import ru.fantlab.android.ui.modules.bookcases.viewer.BookcaseViewerActivity
import ru.fantlab.android.ui.modules.classificator.ClassificatorPagerActivity
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerMvp
import ru.fantlab.android.ui.modules.awards.item.ItemAwardsActivity
import ru.fantlab.android.ui.modules.work.editions.WorkEditionsActivity
import ru.fantlab.android.ui.widgets.dialog.BookcasesDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class WorkOverviewFragment : BaseFragment<WorkOverviewMvp.View, WorkOverviewPresenter>(),
		WorkOverviewMvp.View {

	private lateinit var work: Work
	private val adapterNoms: ItemAwardsAdapter by lazy { ItemAwardsAdapter(arrayListOf()) }
	private val adapterAuthors: WorkAuthorsAdapter by lazy { WorkAuthorsAdapter(arrayListOf()) }
	private val adapterEditions: WorkEditionsAdapter by lazy { WorkEditionsAdapter(arrayListOf()) }
	private val adapterClassification: ClassificationAdapter by lazy { ClassificationAdapter(arrayListOf()) }
	private var pagerCallback: WorkPagerMvp.View? = null

	var inclusions: ArrayList<BookcaseSelection> = arrayListOf()

	override fun fragmentLayout() = R.layout.work_overview_layout

	override fun providePresenter() = WorkOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(
			work: Work,
			rootSagas: ArrayList<WorkRootSaga>,
			awards: ArrayList<Nomination>,
			authors: ArrayList<Work.Author>
	) {
		this.work = work
		pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(work.name)) work.name else work.nameOrig)

		coverLayouts.setUrl("https:${work.image}", WorkTypesProvider.getCoverByTypeId(work.typeId))

		if (isLoggedIn()) {
			presenter.getBookcases("work", work.id, false)
			presenter.getMarks(PrefGetter.getLoggedUser()?.id ?: -1, arrayListOf(work.id))
		} else hideProgress()

		val user = PrefGetter.getLoggedUser()
		if (user != null && user.`class` >= FantlabHelper.Levels.PHILOSOPHER.`class`) {
			classificatorButton.visibility = View.VISIBLE
		} else classificatorButton.visibility = View.GONE

		if (InputHelper.isEmpty(work.name)) {
			workTitle.text = work.nameOrig
			workSubTitle.visibility = View.GONE
		} else {
			workTitle.text = work.name
			if (!InputHelper.isEmpty(work.nameOrig))
				workSubTitle.text = work.nameOrig
			else
				workSubTitle.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(work.lang)) {
			workLang.text = work.lang?.capitalize()
			workLangBlock.visibility = View.VISIBLE
		} else workLangBlock.visibility = View.GONE

		if (work.rating.votersCount.toIntOrNull() != null) {
			ratingBar.rating = work.rating.rating.toFloat()
			rateMark.text = work.rating.rating
			rateCount.text = "(${work.rating.votersCount})"
		} else ratingBar.numStars = 0

		workFullname.text = StringBuilder()
				.append(authors.joinToString { it.name })
				.append(" «")
				.append(if (!InputHelper.isEmpty(work.name)) work.name else work.nameOrig)
				.append("»")

		val roots = StringBuilder()
		rootSagas.forEach {
			if (it.type != null) {
				roots.append(" ")
						.append(it.type.capitalize())
						.append(" «[cycle=${it.id}]${it.name}[/cycle]»")
			}
		}

		workCaption.html = StringBuilder()
				.append(work.type)
				.append(if (work.year != null) ", ${work.year} год" else "")
				.append(if (roots.length > 1) "; $roots" else "")

		if (!InputHelper.isEmpty(work.description))
			annotationText.html = (work.description)
		else annotationBlock.visibility = View.GONE

		if (authors.isNotEmpty()) {
			adapterAuthors.insertItems(authors)
			authorsList.adapter = adapterAuthors
		} else authorsList.visibility = View.GONE

		if (awards.isNotEmpty()) {
			adapterNoms.insertItems(awards)
			awardsList.adapter = adapterNoms
			adapterNoms.listener = presenter
		} else awardsBlock.visibility = View.GONE

		if (pagerCallback?.isCycle() == true) presenter.getContent()

		setEvents(work)
	}

	private fun setEvents(work: Work) {
		myMarkBlock.setOnClickListener {
			if (!isLoggedIn()) {
				showErrorMessage(getString(R.string.unauthorized_user))
			} else showMarkDialog()
		}

		classificatorButton.setOnClickListener { ClassificatorPagerActivity.startActivity(activity!!, work.id) }

		bookcasesButton.setOnClickListener {
			if (!isLoggedIn()) {
				showErrorMessage(getString(R.string.unauthorized_user))
			} else {
				if (inclusions.isNotEmpty()) {
					val dialogView = BookcasesDialogView()
					dialogView.initArguments(getString(R.string.my_bookcases), inclusions)
					dialogView.show(childFragmentManager, "BookcasesDialogView")
				} else showErrorMessage(getString(R.string.no_bookcases))
			}
		}

		responseButton.setOnClickListener {
			startActivityForResult(Intent(activity, EditorActivity::class.java)
					.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_RESPONSE)
					.putExtra(BundleConstant.ID, work.id), BundleConstant.REFRESH_RESPONSE_CODE)
		}

		showAwardsButton.setOnClickListener { ItemAwardsActivity.startActivity(context!!, work.id, workTitle.text.toString(), ItemAwardsActivity.ItemType.WORK) }
		showEditionsButton.setOnClickListener { WorkEditionsActivity.startActivity(context!!, work.id, workTitle.text.toString()) }
		awardsTitle.setOnClickListener { ItemAwardsActivity.startActivity(context!!, work.id, workTitle.text.toString(), ItemAwardsActivity.ItemType.WORK) }
		editionsTitle.setOnClickListener { WorkEditionsActivity.startActivity(context!!, work.id, workTitle.text.toString()) }
	}

	override fun onSetClassification(classificatory: ArrayList<GenreGroup>) {
		hideProgress()

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

	override fun onSetEditions(editions: ArrayList<EditionsBlocks.EditionsBlock>) {
		if (editions.isNotEmpty()) {
			editions.forEach {
				adapterEditions.addItems(it.list)
			}
			editionsList.adapter = adapterEditions
		} else editionsBlock.visibility = View.GONE
	}

	override fun onSetBookcases(inclusions: ArrayList<BookcaseSelection>) {
		this.inclusions = inclusions
		if (inclusions.find { it.included } != null) bookcasesButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
	}

	override fun onSetTranslations(translations: ArrayList<Translation>) {
		if (translations.isNotEmpty()) {
			translationsList.visibility = View.VISIBLE
			val nodes = arrayListOf<TreeNode<*>>()
			translations.forEachIndexed { subIndex, translationLanguage ->
				val langNode = TreeNode(WorkTranslationLanguage(translationLanguage))
				nodes.add(langNode)
				translationLanguage.translations.forEach { translation ->
					nodes[subIndex].addChild(TreeNode(WorkTranslation(translation)))
				}
				langNode.expandAll()
			}
			val adapter = TreeViewAdapter(nodes, Arrays.asList(WorkTranslationViewHolder(), WorkTranslationHeaderViewHolder()))
			if (translationsList.adapter == null) {
				translationsList.adapter = adapter
				adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
					override fun onSelected(extra: Int, add: Boolean) {
					}

					override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
						return false
					}

					override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
					}
				})
			}
			else
				(translationsList.adapter as TreeViewAdapter).refresh(nodes)

		} else translationsList.visibility = View.GONE
	}

	override fun onSetContent(children: ArrayList<ChildWork>) {
		if (children.isNotEmpty()) {
			val nodes = arrayListOf<TreeNode<*>>()
			children.forEachIndexed { index, item ->
				val title = if (item.nameOrig.isNotEmpty() && item.name.isNotEmpty()) item.name else if (item.nameOrig.isNotEmpty() && item.name.isEmpty()) item.nameOrig else item.name
				if (item.deep <= 1) {
					val parent = TreeNode(CycleContentParent(title, item.id ?: -1))
					nodes.add(parent)
				} else if (nodes.size > 0) {
					val parent = nodes[nodes.size - 1]
					val node = if (index + 1 != children.size && children[index + 1].deep == item.deep + 1)
						TreeNode(CycleContentParent(title, item.id ?: -1))
					else
						TreeNode(CycleContentChild(title, item.id ?: -1))
					when (item.deep) {
						2 -> parent.addChild(node)
						3 -> parent.childList[parent.childList.size - 1].addChild(node)
						4 -> parent.childList[parent.childList.size - 1].childList[parent.childList[parent.childList.size - 1].childList.size - 1].addChild(node)
					}
					parent.expandAll()
				}
			}

			val adapter = TreeViewAdapter(nodes, listOf(CycleContentParentViewHolder(), CycleContentChildViewHolder()))
			adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
				override fun onSelected(extra: Int, add: Boolean) {
				}

				override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
					val item = node.content
					var workId = -1
					var workTitle = ""
					if (item is CycleContentChild) {
						workId = item.workId
						workTitle = item.title
					} else if (item is CycleContentParent) {
						workId = item.workId
						workTitle = item.title
					}
					if (workId != -1) {
						WorkPagerActivity.startActivity(activity!!, workId, workTitle)
					}

					return true
				}

				override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				}
			})
			contentList.adapter = adapter
			contentBlock.visibility = View.VISIBLE
		} else contentBlock.visibility = View.GONE
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
		pagerCallback?.onError()
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	private fun showMarkDialog() {
		if (!::work.isInitialized) {
			showErrorMessage(getString(R.string.wait))
			return
		}
		RatingDialogView.newInstance(10, pagerCallback?.onGetMark()?.toFloat() ?: 0f,
				work,
				workFullname.text.toString(),
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

	override fun onBookcaseClick(item: BookcaseSelection, position: Int) {
		val currentUser = PrefGetter.getLoggedUser()
		BookcaseViewerActivity.startActivity(activity!!,
				currentUser?.id ?: -1,
				item.bookcase.bookcaseId,
				item.bookcase.bookcaseName,
				item.bookcase.bookcaseType)
	}

	override fun onBookcaseSelected(item: BookcaseSelection, position: Int) {
		presenter.includeItem(item.bookcase.bookcaseId, work.id, !item.included)
	}

	override fun onBookcaseSelectionUpdated(bookcaseId: Int, include: Boolean) {
		inclusions.find { it.bookcase.bookcaseId == bookcaseId }?.included = include

		if (inclusions.find { it.included } != null) {
			bookcasesButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
		} else bookcasesButton.tintDrawableColor(ViewHelper.getColorAttr(context!!, R.attr.button_icon_color))

		hideProgress()
	}

	override fun onGetMarks(marks: ArrayList<MarkMini>) {
		hideProgress()
		if (marks.isNotEmpty()) {
			if (marks[0].user_work_response_id != 0) {
				responseButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
			}
			if (marks[0].user_work_classif_flag == 1) {
				classificatorButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
			}
			markButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
			myMark.text = marks[0].mark.toString()
			myMark.visibility = View.VISIBLE
			pagerCallback?.onSetMarked(true, marks[0].mark)
		} else pagerCallback?.onSetMarked(false, 0)
	}

	override fun onSetMark(mark: Int, markCount: String, midMark: String) {
		hideProgress()

		ratingBar.rating = midMark.toFloat()
		rateMark.text = midMark
		rateCount.text = "($markCount)"

		if (mark > 0) {
			markButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
			myMark.text = mark.toString()
			myMark.visibility = View.VISIBLE
		}
		pagerCallback?.onSetMarked(mark > 0, mark)
	}

	override fun onRated(rating: Float, listItem: Any, position: Int) {
		presenter.onSendMark((listItem as Work).id, rating.toInt())
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			BundleConstant.REFRESH_RESPONSE_CODE -> {
				responseButton.tintDrawableColor(ContextCompat.getColor(context!!, R.color.gold))
				pagerCallback?.onResponsesRefresh()
			}
			BundleConstant.CLASSIFICATOR_CODE -> {
				if (resultCode == RESULT_OK) {
					presenter.getClassification()
				}
			}
		}
	}

	override fun onSetTitle(title: String) {
		pagerCallback?.onSetTitle(title)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is WorkPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	companion object {

		fun newInstance(workId: Int): WorkOverviewFragment {
			val view = WorkOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, workId).end()
			return view
		}
	}
}
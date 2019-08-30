package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.edition_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.adapter.EditionAuthorsAdapter
import ru.fantlab.android.ui.adapter.viewholder.EditionContentChildViewHolder
import ru.fantlab.android.ui.adapter.viewholder.EditionContentParentViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.bookcases.viewer.BookcaseViewerActivity
import ru.fantlab.android.ui.widgets.Dot
import ru.fantlab.android.ui.widgets.GallerySlider
import ru.fantlab.android.ui.widgets.dialog.BookcasesDialogView
import ru.fantlab.android.ui.widgets.dialog.ListDialogView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter

class EditionOverviewFragment : BaseFragment<EditionOverviewMvp.View, EditionOverviewPresenter>(),
		EditionOverviewMvp.View {

	private lateinit var edition: Edition
	private val adapterAuthors: EditionAuthorsAdapter by lazy { EditionAuthorsAdapter(arrayListOf()) }

	var inclusions: ArrayList<BookcaseSelection> = arrayListOf()

	override fun fragmentLayout() = R.layout.edition_overview_layout

	override fun providePresenter() = EditionOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(edition: Edition, additionalImages: AdditionalImages?) {
		this.edition = edition

		hideProgress()
		coverLayouts.setUrl("https:${edition.image}", WorkTypesProvider.getCoverByTypeId(edition.typeId))
		coverLayouts.setDotColor(
				when {
					edition.planDate.isNotEmpty() -> Dot.Color.GREY
					edition.correctLevel == 0f -> Dot.Color.RED
					edition.correctLevel == 0.5f -> Dot.Color.ORANGE
					edition.correctLevel == 1f -> Dot.Color.GREEN
					else -> throw IllegalStateException("Received invalid edition->correct_level from API")
				}
		)

		coverLayouts.setOnClickListener {
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

		val authors = edition.creators.authors
		if (!authors.isNullOrEmpty()) {
			adapterAuthors.insertItems(authors)
			authorsList.adapter = adapterAuthors
		} else authorsList.visibility = View.GONE

		editionName.text = edition.name
		editionGeneralInfo.text = "${edition.year} г."
		if (edition.series.isNotEmpty()) {
			editionPart.text = edition.series.joinToString(separator = ",\n") { it.name }
			editionPartBlock.visibility = View.VISIBLE
		} else editionPartBlock.visibility = View.GONE

		if (edition.copyCount > 0) {
			editionCount.text = "${edition.copyCount} экз."
			editionCount.visibility = View.VISIBLE
		} else editionCountBlock.visibility = View.GONE

		if (edition.isbns.isNotEmpty()) {
			editionISBN.text = edition.isbns.joinToString(separator = ",\n") { it }
			editionISBNBlock.visibility = View.VISIBLE
		} else editionISBNBlock.visibility = View.GONE

		if (edition.coverType.isNotBlank()) {
			editionCoverType.text = edition.coverType
			coverTypeBlock.visibility = View.VISIBLE
		} else coverTypeBlock.visibility = View.GONE

		if (!InputHelper.isNullEmpty(edition.format)) {
			editionFormat.text = edition.format
			editionFormatBlock.visibility = View.VISIBLE
		} else editionFormatBlock.visibility = View.GONE

		if (edition.pages > 0) {
			editionPages.text = edition.pages.toString()
			editionPagesBlock.visibility = View.VISIBLE
		} else editionPagesBlock.visibility = View.GONE

		editionTitle.text = StringBuilder()
				.append(if (authors?.isNotEmpty() == true) authors.joinToString { it.name } else "")
				.append(" «")
				.append(edition.name)
				.append("»")
		workCaption.text = StringBuilder()
				.append(edition.type)
				.append(if (edition.additionalTypes.isNotEmpty()) edition.additionalTypes.joinToString (prefix = ", ") { it } else "")

		if (edition.description.isNotBlank()) {
			descriptionText.html = edition.description
			descriptionBlock.visibility = View.VISIBLE
		} else descriptionBlock.visibility = View.GONE

		if (edition.notes.isNotBlank()) {
			noteText.html = edition.notes
			noteBlock.visibility = View.VISIBLE
		} else noteBlock.visibility = View.GONE

		bookcasesButton.setOnClickListener {
			if (inclusions.isNotEmpty()) {
				val dialogView = BookcasesDialogView()
				dialogView.initArguments(getString(R.string.my_bookcases), inclusions)
				dialogView.show(childFragmentManager, "BookcasesDialogView")
			} else showErrorMessage(getString(R.string.no_bookcases))
		}

		if (isLoggedIn()) presenter.getBookcases("edition", edition.id, false)
	}

	override fun onSetContent(content: ArrayList<EditionContent>) {
		val nodes = arrayListOf<TreeNode<*>>()
		content.forEachIndexed { index, item ->
			if (item.level <= 1) {
				val parent = TreeNode(EditionContentParent(item.title))
				nodes.add(parent)
			} else if (nodes.size > 0) {
				val parent = nodes[nodes.size - 1]
				val node = if (index + 1 != content.size && content[index + 1].level == item.level + 1)
					TreeNode(EditionContentParent(item.title))
				else
					TreeNode(EditionContentChild(item.title))
				when (item.level) {
					2 -> parent.addChild(node)
					3 -> parent.childList[parent.childList.size - 1].addChild(node)
					4 -> parent.childList[parent.childList.size - 1].childList[parent.childList[parent.childList.size - 1].childList.size - 1].addChild(node)
				}
				parent.expandAll()
			}
		}

		val adapter = TreeViewAdapter(nodes, listOf(EditionContentParentViewHolder(), EditionContentChildViewHolder()))
		contentList.adapter = adapter
	}

	override fun onSetBookcases(inclusions: ArrayList<BookcaseSelection>) {
		this.inclusions = inclusions
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

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.authors -> {
				val authorsList: ArrayList<Edition.Author> = edition.creators.authors!!
				val dialogView: ListDialogView<Edition.Author> = ListDialogView()
				dialogView.initArguments(getString(R.string.authors), authorsList)
				dialogView.show(childFragmentManager, "ListDialogView")
			}
		}
	}

	override fun <T> onItemSelected(item: T, position: Int) {
		AuthorPagerActivity.startActivity(context!!, (item as Edition.Author).id, item.name, 0)
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
		presenter.includeItem(item.bookcase.bookcaseId, edition.id, !item.included)
	}

	override fun onBookcaseSelectionUpdated(bookcaseId: Int, include: Boolean) {
		inclusions.find { it.bookcase.bookcaseId == bookcaseId }?.included = include
		hideProgress()
	}

	companion object {

		@Keep
		val TAG: String = EditionOverviewFragment::class.java.simpleName

		fun newInstance(editionId: Int): EditionOverviewFragment {
			val view = EditionOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, editionId).end()
			return view
		}
	}
}
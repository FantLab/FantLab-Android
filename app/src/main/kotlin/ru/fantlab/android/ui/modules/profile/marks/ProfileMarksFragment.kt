package ru.fantlab.android.ui.modules.profile.marks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.MarksSortOption
import ru.fantlab.android.provider.rest.MarksTypeOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ProfileMarksAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.user.UserPagerMvp
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.chartbar.ChartBar
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView

class ProfileMarksFragment : BaseFragment<ProfileMarksMvp.View, ProfileMarksPresenter>(),
		ProfileMarksMvp.View {

	@State var userId: Int = -1
	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, userId) }
	private val adapter: ProfileMarksAdapter by lazy { ProfileMarksAdapter(arrayListOf()) }
	private var countCallback: UserPagerMvp.View? = null

	override fun fragmentLayout(): Int = R.layout.micro_grid_refresh_list

	override fun providePresenter(): ProfileMarksPresenter = ProfileMarksPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			stateLayout.hideProgress()
		}
		stateLayout.setEmptyText(R.string.no_marks)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		recycler.adapter = adapter
		userId = arguments!!.getInt(BundleConstant.EXTRA)
		presenter.getMarks(userId, false)
		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is UserPagerMvp.View) {
			countCallback = context
		}
	}

	override fun onDetach() {
		countCallback = null
		super.onDetach()
	}

	override fun onDestroyView() {
		recycler.removeOnScrollListener(getLoadMore())
		super.onDestroyView()
	}

	override fun onNotifyAdapter(items: ArrayList<Mark>, page: Int) {
		hideProgress()
		if (items.isEmpty()) {
			adapter.clear()
			stateLayout.showEmptyState()
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun onSetMark(position: Int, mark: Int) {
		hideProgress()
		if (mark == 0) {
			adapter.removeItem(position)
			onSetTabCount(adapter.itemCount)
		} else {
			adapter.getItem(position).mark = mark
			adapter.notifyItemChanged(position)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetBadge(1, count)
	}

	override fun onItemClicked(item: Mark) {
		if (item.workTypeId == FantlabHelper.WorkType.CYCLE.id)
			CyclePagerActivity.startActivity(context!!, item.workId, item.workName, 0)
		else
			WorkPagerActivity.startActivity(context!!, item.workId, item.workName, 0)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Mark) {
		if (isLoggedIn() && PrefGetter.getLoggedUser()?.id == userId) {
			val dialogView = ContextMenuDialogView()
			dialogView.initArguments("main", ContextMenuBuilder.buildForMarks(recycler.context), item, position)
			dialogView.show(childFragmentManager, "ContextMenuDialogView")
		}
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		if (listItem is Mark) when (item.id) {
			"revote" -> {
				RatingDialogView.newInstance(10, listItem.mark.toFloat(),
						listItem,
						"${listItem.workAuthor} - ${listItem.workName}",
						position
				).show(childFragmentManager, RatingDialogView.TAG)
			}
			"delete" -> {
				presenter.onSendMark(listItem, 0, position)
			}
		} else {
			when (item.id) {
				"chart" -> {
					val stats = presenter.stats ?: return
					val points: ArrayList<Pair<String, Int>> = arrayListOf()
					if (position == 0)
						stats.marksStats.map { points.add(Pair(it.mark.toString(), it.markCount)) }
					else
						stats.worksStats.map { points.add(Pair(it.workType, it.markCount)) }

					ChartBar.newInstance(
							title = item.title,
							points = points,
							colored = position == 0
					).show(childFragmentManager, ChartBar.TAG)
				}
				"sort" -> {
					presenter.setCurrentSort(MarksSortOption.values()[position], null)
				}
				else -> {
					when (parent) {
						"category" -> {
							presenter.setCurrentSort(null, MarksTypeOption.values()[position])
						}
					}
				}
			}
		}
	}

	override fun onRated(rating: Float, listItem: Any, position: Int) {
		presenter.onSendMark(listItem as Mark, rating.toInt(), position)
	}

	override fun onRefresh() {
		presenter.getMarks(userId, true)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		callback?.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		showReload()
		super.showMessage(titleRes, msgRes)
	}

	private fun showReload() {
		hideProgress()
		stateLayout.showReload(adapter.itemCount)
	}

	fun showChartsDialog() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForMarksCharts(recycler.context))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("sort", ContextMenuBuilder.buildForProfileMarksSorting(recycler.context, sort.sortBy))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	fun showFilterDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("filter", ContextMenuBuilder.buildForProfileMarksFilter(recycler.context, sort.filterCategory))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	companion object {

		fun newInstance(userId: Int): ProfileMarksFragment {
			val view = ProfileMarksFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
			return view
		}
	}
}
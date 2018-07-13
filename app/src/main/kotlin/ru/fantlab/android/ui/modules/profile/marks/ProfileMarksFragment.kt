package ru.fantlab.android.ui.modules.profile.marks

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.ProfileMarksAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.user.UserPagerMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller

class ProfileMarksFragment : BaseFragment<ProfileMarksMvp.View, ProfileMarksPresenter>(),
		ProfileMarksMvp.View {

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller
	@State var userId: Int? = null
	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, userId) }
	private val adapter: ProfileMarksAdapter by lazy { ProfileMarksAdapter(presenter.getMarks()) }
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
		recycler.addKeyLineDivider()
		if (savedInstanceState == null) {
			userId = arguments?.getInt(BundleConstant.EXTRA)
		}
		if (presenter.getMarks().isEmpty() && !presenter.isApiCalled()) {
			onRefresh()
		}
		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		fastScroller.attachRecyclerView(recycler)
	}

	override fun onAttach(context: Context?) {
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
			return
		}
		if (page <= 1) {
			adapter.insertItems(items)
		} else {
			adapter.addItems(items)
		}
	}

	override fun getLoadMore() = onLoadMore

	override fun onSetTabCount(count: Int) {
		countCallback?.onSetBadge(1, count)
	}

	override fun onItemClicked(item: Mark) {
		WorkPagerActivity.startActivity(context!!, item.workId, item.workName, 0)
	}

	override fun onRefresh() {
		presenter.onCallApi(1, userId)
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

	override fun showErrorMessage(msgRes: String) {
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

	companion object {

		fun newInstance(userId: Int): ProfileMarksFragment {
			val view = ProfileMarksFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
			return view
		}
	}
}
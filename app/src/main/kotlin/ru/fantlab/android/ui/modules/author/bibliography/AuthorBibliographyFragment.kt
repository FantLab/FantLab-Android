package ru.fantlab.android.ui.modules.author.bibliography

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Cycle
import ru.fantlab.android.data.dao.model.CycleWork
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.viewholder.CycleViewHolder
import ru.fantlab.android.ui.adapter.viewholder.CycleWorkViewHolder
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerMvp
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.StateLayout
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import ru.fantlab.android.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import java.util.*


class AuthorBibliographyFragment : BaseFragment<AuthorBibliographyMvp.View, AuthorBibliographyPresenter>(),
		AuthorBibliographyMvp.View {

	private var bibliography: WorksBlocks? = null

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list
	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
	@BindView(R.id.refresh) lateinit var refresh: SwipeRefreshLayout
	@BindView(R.id.stateLayout) lateinit var stateLayout: StateLayout
	@BindView(R.id.fastScroller) lateinit var fastScroller: RecyclerViewFastScroller

	private var countCallback: AuthorPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			bibliography = savedInstanceState.getParcelable("bibliography")
			if (bibliography != null) {
				onInitViews(bibliography!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun providePresenter() = AuthorBibliographyPresenter()

	override fun onInitViews(authorBibliographyResponse: WorksBlocks?) {
		hideProgress()
		stateLayout.setEmptyText(R.string.no_bibliography)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		recycler.addDivider()
		fastScroller.attachRecyclerView(recycler)
		initAdapter(authorBibliographyResponse)
	}

	private fun initAdapter(bibliography: WorksBlocks?) {
		val nodes = arrayListOf<TreeNode<*>>()

		bibliography?.worksBlocks?.forEach { worksBlock ->
			val app = TreeNode(Cycle(worksBlock.title))
			nodes.add(app)

			worksBlock.list.forEachIndexed { subIndex, work ->

				val name = if (work.name.isNotEmpty()) {
					if (work.nameOrig.isNotEmpty()) {
						String.format("%s / %s", work.name, work.nameOrig)
					} else {
						work.name
					}
				} else {
					work.nameOrig
				}

				val apps = TreeNode(Cycle(name))
				app.addChild(apps)

				work.children?.forEach{

					val nameSubWork = if (it.name.isNotEmpty()) {
						if (it.nameOrig.isNotEmpty()) {
							String.format("%s / %s", it.name, it.nameOrig)
						} else {
							it.name
						}
					} else {
						it.nameOrig
					}
					app.childList[subIndex].addChild(TreeNode(CycleWork(nameSubWork, it.id ?: 0)))
				}
			}
		}

		val adapter = TreeViewAdapter(nodes, Arrays.asList(CycleWorkViewHolder(), CycleViewHolder()))
		recycler.adapter = adapter
		adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
			override fun onSelected(extra: Int, add: Boolean) {
			}
			override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
				if (!node.isLeaf) {
					onToggle(!node.isExpand, holder)
				} else {
					val cycleWork = node.content as CycleWork
					if (cycleWork.workId != 0) WorkPagerActivity.startActivity(context!!, cycleWork.workId, cycleWork.title, 0)
				}
				return false
			}
			override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
				val dirViewHolder = holder as CycleViewHolder.ViewHolder
				val ivArrow = dirViewHolder.ivArrow
				val rotateDegree = if (isExpand) 90.0f else -90.0f
				ivArrow.animate().rotationBy(rotateDegree)
						.start()
			}
		})

	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("bibliography", bibliography)
	}

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        refresh.isRefreshing = true
        stateLayout.showProgress()
    }

    override fun hideProgress() {
        refresh.isRefreshing = false
        stateLayout.hideProgress()
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

		fun newInstance(authorId: Int): AuthorBibliographyFragment {
			val view = AuthorBibliographyFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, authorId).end()
			return view
		}
	}

    override fun onRefresh() {
        presenter.onCallApi()
    }

    override fun onClick(v: View?) {
        onRefresh()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AuthorPagerMvp.View) {
            countCallback = context
        }
    }

    override fun onDetach() {
        countCallback = null
        super.onDetach()
    }

}
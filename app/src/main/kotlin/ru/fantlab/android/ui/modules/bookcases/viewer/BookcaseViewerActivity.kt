package ru.fantlab.android.ui.modules.bookcases.viewer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.View
import android.view.MenuItem
import android.widget.Toast
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import com.evernote.android.state.State
import es.dmoral.toasty.Toasty
import ru.fantlab.android.helper.*
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.BookcaseEditionsAdapter
import ru.fantlab.android.ui.adapter.BookcaseFilmsAdapter
import ru.fantlab.android.ui.adapter.BookcaseWorksAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.bookcases.editor.BookcaseEditorActivty
import ru.fantlab.android.ui.widgets.dialog.MessageDialogView

class BookcaseViewerActivity : BaseActivity<BookcaseViewerMvp.View, BookcaseViewerPresenter>(), BookcaseViewerMvp.View {

    @State var bookcaseId: Int = 0
    @State var bookcaseName: String = ""
    @State var bookcaseType: String = ""
    @State var bookcaseDescription: String = ""
    @State var bookcaseShared: Int = 0

    private val editionsAdapter: BookcaseEditionsAdapter by lazy { BookcaseEditionsAdapter(arrayListOf()) }
    private val worksAdapter: BookcaseWorksAdapter by lazy { BookcaseWorksAdapter(arrayListOf()) }
    private val filmsAdapter: BookcaseFilmsAdapter by lazy { BookcaseFilmsAdapter(arrayListOf()) }

    private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter, bookcaseId) }

    private lateinit var toolbarMenu: Menu

    override fun layout(): Int = R.layout.bookcase_viewer_layout

    override fun isTransparent(): Boolean = false

    override fun canBack(): Boolean = true

    override fun providePresenter(): BookcaseViewerPresenter = BookcaseViewerPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            bookcaseId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
            bookcaseName = intent?.extras?.getString(BundleConstant.EXTRA_TWO, "") ?: ""
            bookcaseType = intent?.extras?.getString(BundleConstant.EXTRA_FOUR, "") ?: ""
            bookcaseDescription = intent?.extras?.getString(BundleConstant.EXTRA_FIVE, "") ?: ""
            bookcaseShared = intent?.extras?.getInt(BundleConstant.EXTRA_SIX, -1) ?: -1
        }
        if (bookcaseId == -1) {
            finish()
            return
        }
        presenter.setBookcaseType(bookcaseType)
        title = bookcaseName
        hideShowShadow(true)
        if (savedInstanceState == null) {
            stateLayout.hideProgress()
        }
        stateLayout.setEmptyText(R.string.no_results)
        stateLayout.setOnReloadListener(this)
        refresh.setOnRefreshListener(this)
        recycler.setEmptyView(stateLayout, refresh)
        recycler.addDivider()
        getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
        recycler.addOnScrollListener(getLoadMore())
        fastScroller.attachRecyclerView(recycler)

        when (bookcaseType) {
            "edition" -> {
                editionsAdapter.itemCommentUpdateListener = presenter
                editionsAdapter.itemDeletionListener = presenter
                recycler.adapter = editionsAdapter
            }
            "work" -> {
                worksAdapter.itemCommentUpdateListener = presenter
                worksAdapter.itemDeletionListener = presenter
                recycler.adapter = worksAdapter
            }
            "film" -> {
                filmsAdapter.itemCommentUpdateListener = presenter
                filmsAdapter.itemDeletionListener = presenter
                recycler.adapter = filmsAdapter
            }
        }
        presenter.onCallApi(1, bookcaseId)
    }

    override fun getLoadMore() = onLoadMore

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bookcase_menu, menu)
        toolbarMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookcaseEdit -> {
                BookcaseEditorActivty.startActivityForUpdate(this, bookcaseId, bookcaseName,
                        bookcaseType, bookcaseDescription, bookcaseShared)
            }
            R.id.bookcaseDelete -> {
                MessageDialogView.newInstance(
                        bundleTitle = getString(R.string.bookcase_deleting),
                        bundleMsg = getString(R.string.confirm_message),
                        bundle = Bundler.start()
                                .put(BundleConstant.YES_NO_EXTRA, true)
                                .put("bookcase_deletion", true)
                                .end()
                ).show(supportFragmentManager, MessageDialogView.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMessageDialogActionClicked(isOk: Boolean, bundle: Bundle?) {
        if (isOk && bundle != null) {
            val bookcaseDeletion = bundle.getBoolean("bookcase_deletion")
            val itemDeletion = bundle.getBoolean("bookcase_item_deletion")
            val commentUpdate = bundle.getBoolean("bookcase_item_comment")
            when {
                bookcaseDeletion -> presenter.deleteBookcase(bookcaseId)
                itemDeletion -> {
                    val itemId = bundle.getInt("bookcase_item_id")
                    presenter.excludeItem(bookcaseId, itemId)
                }
                commentUpdate -> {
                    val itemId = bundle.getInt("bookcase_item_id")
                    val resComment = bundle.getString("edit_text")
                    presenter.updateComment(bookcaseId, itemId, resComment)
                }
            }
        }
    }

    override fun onSuccessfullyDeleted() {
        Toasty.info(applicationContext!!, getString(R.string.bookcase_deleted), Toast.LENGTH_LONG).show()
        val intent = Intent()
        intent.putExtras(Bundler.start()
                .put(BundleConstant.EXTRA, true)
                .end())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNotifyEditionsAdapter(items: ArrayList<BookcaseEdition>, page: Int) {
        hideProgress()
        if (items.isEmpty()) {
            editionsAdapter.clear()
            return
        }
        if (page <= 1) {
            editionsAdapter.insertItems(items)
        } else {
            editionsAdapter.addItems(items)
        }
    }

    override fun onNotifyWorksAdapter(items: ArrayList<BookcaseWork>, page: Int) {
        hideProgress()
        if (items.isEmpty()) {
            worksAdapter.clear()
            return
        }
        if (page <= 1) {
            worksAdapter.insertItems(items)
        } else {
            worksAdapter.addItems(items)
        }
    }

    override fun onNotifyFilmsAdapter(items: ArrayList<BookcaseFilm>, page: Int) {
        hideProgress()
        if (items.isEmpty()) {
            filmsAdapter.clear()
            return
        }
        if (page <= 1) {
            filmsAdapter.insertItems(items)
        } else {
            filmsAdapter.addItems(items)
        }
    }

    override fun onRefresh() {
        when (bookcaseType) {
            "edition" -> {
                presenter.getEditions(true, bookcaseId, 1)
            }
            "work" -> {
                presenter.getWorks(true, bookcaseId, 1)
            }
            "film" -> {
                presenter.getFilms(true, bookcaseId, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == BundleConstant.BOOKCASE_EDITOR) {
                bookcaseName = data?.extras?.getString(BundleConstant.EXTRA_THREE, "") ?: ""
                bookcaseDescription = data?.extras?.getString(BundleConstant.EXTRA_FIVE, "") ?: ""
                bookcaseShared = data?.extras?.getInt(BundleConstant.EXTRA_SIX, 0) ?: 1
                title = bookcaseName
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun showMessage(titleRes: Int, msgRes: Int) {
        showReload()
        super.showMessage(titleRes, msgRes)
    }

    private fun showReload() {
        hideProgress()
        stateLayout.showReload(recycler.adapter.itemCount)
    }

    override fun onDeleteItemFromBookcase(itemId: Int) {
        MessageDialogView.newInstance(
                bundleTitle = getString(R.string.bookcase_item_deleting),
                bundleMsg = getString(R.string.confirm_message),
                bundle = Bundler.start()
                        .put(BundleConstant.YES_NO_EXTRA, true)
                        .put("bookcase_item_deletion", true)
                        .put("bookcase_item_id", itemId)
                        .end()
        ).show(supportFragmentManager, MessageDialogView.TAG)
    }

    override fun onUpdateItemComment(itemId: Int, itemComment: String?) {
        MessageDialogView.newInstanceForEdit(
                bundleTitle = getString(R.string.bookcase_item_comment_modifying),
                bundleMsg = itemComment ?: "",
                bundle = Bundler.start()
                        .put(BundleConstant.YES_NO_EXTRA, true)
                        .put("bookcase_item_comment", true)
                        .put("bookcase_item_id", itemId)
                        .end()
        ).show(supportFragmentManager, MessageDialogView.TAG)
    }

    companion object {

        fun startActivity(activity: Activity,
                          bookcaseId: Int,
                          bookcaseName: String,
                          bookcaseType: String,
                          bookcaseDescription: String,
                          bookcaseShared: Int) {
            val intent = Intent(activity, BookcaseViewerActivity::class.java)
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, bookcaseId)
                    .put(BundleConstant.EXTRA_TWO, bookcaseName)
                    .put(BundleConstant.EXTRA_FOUR, bookcaseType)
                    .put(BundleConstant.EXTRA_FIVE, bookcaseDescription)
                    .put(BundleConstant.EXTRA_SIX, bookcaseShared)
                    .end())
            activity.startActivityForResult(intent, BundleConstant.BOOKCASE_VIEWER)
        }
    }
}
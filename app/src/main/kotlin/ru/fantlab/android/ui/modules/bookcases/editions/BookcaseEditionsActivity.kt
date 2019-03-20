package ru.fantlab.android.ui.modules.bookcases.editions

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
import ru.fantlab.android.ui.adapter.BookcaseEditionsAdapter
import ru.fantlab.android.ui.adapter.BookcaseFilmsAdapter
import ru.fantlab.android.ui.adapter.BookcaseWorksAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.dialog.MessageDialogView

class BookcaseEditionsActivity : BaseActivity<BookcaseEditionsMvp.View, BookcaseEditionsPresenter>(), BookcaseEditionsMvp.View {

    @State var bookcaseId: Int = 0
    @State var bookcaseName: String = ""
    @State var bookcaseType: String = ""
    @State var userId: Int = -1

    private val editionsAdapter: BookcaseEditionsAdapter by lazy { BookcaseEditionsAdapter(arrayListOf()) }
    private val worksAdapter: BookcaseWorksAdapter by lazy { BookcaseWorksAdapter(arrayListOf()) }
    private val filmsAdapter: BookcaseFilmsAdapter by lazy { BookcaseFilmsAdapter(arrayListOf()) }

    private lateinit var toolbarMenu: Menu

    override fun layout(): Int = R.layout.bookcase_editions_layout

    override fun isTransparent(): Boolean = false

    override fun canBack(): Boolean = true

    override fun providePresenter(): BookcaseEditionsPresenter = BookcaseEditionsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            bookcaseId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
            bookcaseName = intent?.extras?.getString(BundleConstant.EXTRA_TWO, "") ?: ""
            userId = intent?.extras?.getInt(BundleConstant.EXTRA_THREE, -1) ?: -1
            bookcaseType = intent?.extras?.getString(BundleConstant.EXTRA_FOUR, "") ?: ""
        }
        if (bookcaseId == -1 || userId == -1) {
            finish()
            return
        }

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
        fastScroller.attachRecyclerView(recycler)

        when (bookcaseType) {
            "edition" -> {
                recycler.adapter = editionsAdapter
                presenter.getEditions(false, bookcaseId)
            }
            "work" -> {
                recycler.adapter = worksAdapter
                presenter.getWorks(false, bookcaseId)
            }
            "film" -> {
                recycler.adapter = filmsAdapter
                presenter.getFilms(false, bookcaseId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bookcase_menu, menu)
        toolbarMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookcaseEdit -> {
                // TODO not implemented
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
            val deletion = bundle.getBoolean("bookcase_deletion")
            if (deletion) {
                presenter.deleteBookcase(bookcaseId, userId)
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

    override fun onNotifyEditionsAdapter(items: ArrayList<BookcaseEdition>) {
        hideProgress()
        if (items.isEmpty()) {
            editionsAdapter.clear()
            return
        }
        editionsAdapter.insertItems(items)
    }

    override fun onNotifyWorksAdapter(items: ArrayList<BookcaseWork>) {
        hideProgress()
        if (items.isEmpty()) {
            worksAdapter.clear()
            return
        }
        worksAdapter.insertItems(items)
    }

    override fun onNotifyFilmsAdapter(items: ArrayList<BookcaseFilm>) {
        hideProgress()
        if (items.isEmpty()) {
            filmsAdapter.clear()
            return
        }
        filmsAdapter.insertItems(items)
    }

    override fun onRefresh() {
        when (bookcaseType) {
            "edition" -> {
                presenter.getEditions(true, bookcaseId)
            }
            "work" -> {
                presenter.getWorks(true, bookcaseId)
            }
            "film" -> {
                presenter.getFilms(true, bookcaseId)
            }
        }
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

    companion object {

        fun startActivity(activity: Activity, bookcaseId: Int, bookcaseName: String, userId: Int, bookcaseType: String) {
            val intent = Intent(activity, BookcaseEditionsActivity::class.java)
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, bookcaseId)
                    .put(BundleConstant.EXTRA_TWO, bookcaseName)
                    .put(BundleConstant.EXTRA_THREE, userId)
                    .put(BundleConstant.EXTRA_FOUR, bookcaseType)
                    .end())
            activity.startActivityForResult(intent, BundleConstant.BOOKCASE_VIEWER)
        }
    }
}
package ru.fantlab.android.ui.modules.work.responses.overview

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class ResponseActivity : BaseActivity<ResponseOverviewMvp.View, ResponseOverviewPresenter>(),
		ResponseOverviewMvp.View {

	@State var workId: Int = 0
	@State var workName: String = ""
	private var response: Response? = null

	override fun layout(): Int = R.layout.response_layout
	@JvmField @BindView(R.id.coverLayout) var coverLayout: CoverLayout? = null
	@BindView(R.id.date) lateinit var date: FontTextView
	@BindView(R.id.username) lateinit var username: FontTextView
	@BindView(R.id.workName) lateinit var workTitle: FontTextView
	@BindView(R.id.text) lateinit var text: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	@BindView(R.id.votes) lateinit var votes: FontTextView

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

    override fun providePresenter(): ResponseOverviewPresenter = ResponseOverviewPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
            response = intent?.extras?.getParcelable(BundleConstant.EXTRA)
			workId = intent?.extras?.getInt(BundleConstant.EXTRA_TWO, -1) ?: -1
			workName = intent?.extras?.getString(BundleConstant.EXTRA_THREE) ?: ""
            presenter.initResponse(response)
		}
		if (workId == -1) {
			finish()
			return
		}
		title = getString(R.string.view_response)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.share_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
        response
		when (item.itemId) {
			R.id.share -> {
                ActivityHelper.shareUrl(this, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendEncodedPath("work$workId#responses")
						.toString())
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

    companion object {
        fun startActivity(context: Context, response: Response, workId: Int = -1, workName: String) {
            val intent = Intent(context, ResponseActivity::class.java)
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, response)
                    .put(BundleConstant.EXTRA_TWO, workId)
                    .put(BundleConstant.EXTRA_THREE, workName)
                    .end())
            if (context is Service || context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onInitViews(response: Response) {
        coverLayout?.setUrl("https:${response.userAvatar}")
        username.text = response.userName
		username.setOnClickListener {
			val dialogView = ContextMenuDialogView()
			dialogView.initArguments("main", ContextMenuBuilder.buildForProfile(this), response, 0)
			dialogView.show(supportFragmentManager, "ContextMenuDialogView")
		}
		date.text = response.dateIso.parseFullDate(true).getTimeAgo()

        workTitle.text = if (response.workName.isNotEmpty()) {
            if (response.workNameOrig.isNotEmpty()) {
                String.format("%s / %s", response.workName, response.workNameOrig)
            } else {
                response.workName
            }
        } else {
            response.workNameOrig
        }

        text.text = response.text
                .replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
                .replace("\\[.*]".toRegex(), "")       // bb-коды
                .replace(":\\w+:".toRegex(), "")       // смайлы

        if (response.mark == null) {
            rating.visibility = View.GONE
        } else {
            rating.text = response.mark.toString()
            rating.visibility = View.VISIBLE
        }

        votes.text = response.voteCount.toString()
    }

	override fun onSetVote(position: Int, votesCount: String) {
		hideProgress()
	}

	override fun onItemSelected(item: ContextMenus.MenuItem, listItem: Any, position: Int) {
		listItem as Response
		when (item.id){
			"vote" -> {
				presenter.onSendVote(listItem, position, if (item.title.contains("+")) "plus" else "minus")
			}
			"profile" -> {
				UserPagerActivity.startActivity(this, listItem.userName, listItem.userId,0 )
			}
			"message" -> {
				startActivity(Intent(this, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
						.putExtra(BundleConstant.ID, listItem.userId)
				)
			}
		}
	}

}
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
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.response_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.editor.EditorActivity
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.modules.work.CyclePagerActivity
import ru.fantlab.android.ui.modules.work.WorkPagerActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

class ResponseOverviewActivity : BaseActivity<ResponseOverviewMvp.View, ResponseOverviewPresenter>(),
		ResponseOverviewMvp.View {

	@State lateinit var response: Response

	override fun layout(): Int = R.layout.response_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): ResponseOverviewPresenter = ResponseOverviewPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			response = intent!!.extras.getParcelable(BundleConstant.EXTRA)
			onInitViews(response)
		} else onInitViews(response)
		if (response.id == -1) {
			finish()
			return
		}
		fab.setOnClickListener { onFabClicked() }
		title = getString(R.string.view_response)
		hideShowShadow(true)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.share_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.share -> {
				ActivityHelper.shareUrl(this, Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
						.authority(LinkParserHelper.HOST_DEFAULT)
						.appendEncodedPath("work${response.workId}#response${response.id}")
						.toString())
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onInitViews(response: Response) {
		avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${response.userId}")
		responseUser.text = response.userName.capitalize()

		action.text = String.format(getString(R.string.response), if (response.userSex == "m") getString(R.string.left) else getString(R.string.left_female))
		date.text = response.dateIso.parseFullDate(true).getTimeAgo()

		authors.text = if (!InputHelper.isEmpty(response.workAuthor)) response.workAuthor else response.workAuthorOrig

		workName.text = if (response.workName.isNotEmpty()) response.workName else response.workNameOrig
		workName.setOnClickListener { openWorkPager() }

		coverLayout.setUrl("https:${response.workImage}", WorkTypesProvider.getCoverByTypeId(response.workTypeId))
		coverLayout.setOnClickListener { openWorkPager() }

		userInfo.setOnClickListener { showUserMenu() }

		responseText.text = response.text
				.replace("(\r\n)+".toRegex(), "\n")
				.replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
				.replace("\\[.*]".toRegex(), "")
				.replace(":\\w+:".toRegex(), "")

		if (response.mark == null) {
			rating.visibility = View.GONE
		} else {
			rating.text = response.mark.toString()
			rating.visibility = View.VISIBLE
		}

		response.voteCount.let {
			when {
				it < 0 -> {
					votes.setDrawables(R.drawable.ic_thumb_down)
					votes.text = response.voteCount.toString()
					votes.visibility = View.VISIBLE
				}
				it > 0 -> {
					votes.setDrawables(R.drawable.ic_thumb_up)
					votes.text = response.voteCount.toString()
					votes.visibility = View.VISIBLE
				}
				else -> votes.visibility = View.GONE
			}
		}

		if (isLoggedIn() && PrefGetter.getLoggedUser()?.id != response.userId) {
			fab.visibility = View.VISIBLE
		} else {
			fab.visibility = View.GONE
		}
	}

	private fun openWorkPager() {
		if (response.workTypeId == FantlabHelper.WorkType.CYCLE.id)
			CyclePagerActivity.startActivity(this, response.workId, response.workName, 0)
		else
			WorkPagerActivity.startActivity(this, response.workId, response.workName, 0)
	}

	private fun showUserMenu() {
		val dialogView = ContextMenuDialogView()
		dialogView.initArguments("main", ContextMenuBuilder.buildForProfile(responseUser.context), response, 0)
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
	}

	fun onFabClicked() {
		presenter.onGetUserLevel()
	}

	override fun onShowVotesDialog(userLevel: Float) {
		hideProgress()
		val dialogView = ContextMenuDialogView()
		val variants = ContextMenuBuilder.buildForResponse(this)
		if (userLevel < FantlabHelper.minLevelToVote) variants[0].items.removeAt(1)
		dialogView.initArguments("votes", variants, response, 0)
		dialogView.show(supportFragmentManager, "ContextMenuDialogView")
	}

	override fun onSetVote(votesCount: String) {
		hideProgress()
		votes.text = votesCount
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		listItem as Response
		when (item.id) {
			"vote" -> {
				presenter.onSendVote(listItem, if (item.title.contains("+")) "plus" else "minus")
			}
			"profile" -> {
				UserPagerActivity.startActivity(this, listItem.userName, listItem.userId, 0)
			}
			"message" -> {
				startActivity(Intent(this, EditorActivity::class.java)
						.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_NEW_MESSAGE)
						.putExtra(BundleConstant.ID, listItem.userId)
				)
			}
		}
	}

	companion object {
		fun startActivity(context: Context, response: Response) {
			val intent = Intent(context, ResponseOverviewActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, response)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}
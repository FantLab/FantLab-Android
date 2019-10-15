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
import kotlinx.android.synthetic.main.appbar_response_layout.*
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
		title = "«" + (if (response.workName.isNotEmpty()) response.workName else response.workNameOrig) + "»"
		toolbar?.subtitle = getString(R.string.view_response)
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
		avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/users/${response.userId}")
		responseUser.text = response.userName.capitalize()

		date.text = response.dateIso.parseFullDate(true).getTimeAgo()

		authors.text = if (!InputHelper.isEmpty(response.workAuthor)) response.workAuthor else response.workAuthorOrig

		workName.text = if (response.workName.isNotEmpty()) response.workName else response.workNameOrig
		workBlock.setOnClickListener { openWorkPager() }

		coverLayout.setUrl("https:${response.workImage}", WorkTypesProvider.getCoverByTypeId(response.workTypeId))

		userInfo.setOnClickListener { showUserMenu() }

		responseText.html = response.text

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
			fab.setImageResource(R.drawable.ic_thumb_up_down)
			fab.visibility = View.VISIBLE
		} else if (isLoggedIn()) {
			fab.setImageResource(R.drawable.ic_response)
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

	private fun onFabClicked() {
		if (PrefGetter.getLoggedUser()?.id == response.userId) {
			startActivityForResult(Intent(this, EditorActivity::class.java)
					.putExtra(BundleConstant.EXTRA_TYPE, BundleConstant.EDITOR_EDIT_RESPONSE)
					.putExtra(BundleConstant.EXTRA, response.text)
					.putExtra(BundleConstant.EXTRA_TWO, response.id)
					.putExtra(BundleConstant.ID, response.workId),
					BundleConstant.REFRESH_RESPONSE_CODE
			)
		} else presenter.onGetUserLevel()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			BundleConstant.REFRESH_RESPONSE_CODE -> {
				if (data != null) {
					val responseNewText = data.extras?.getCharSequence(BundleConstant.EXTRA)
					responseText.html = responseNewText
					response.text = responseNewText.toString()
				}
			}
		}
	}

	override fun onShowVotesDialog(userLevel: Float) {
		hideProgress()
		val dialogView = ContextMenuDialogView()
		val variants = ContextMenuBuilder.buildForResponse()
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
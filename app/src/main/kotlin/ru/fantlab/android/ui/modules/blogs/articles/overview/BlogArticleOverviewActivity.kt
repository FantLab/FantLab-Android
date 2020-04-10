package ru.fantlab.android.ui.modules.blogs.articles.overview

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.appbar_blog_article_layout.*
import kotlinx.android.synthetic.main.blog_article_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.BlogArticle
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView


class BlogArticleOverviewActivity : BaseActivity<BlogArticleOverviewMvp.View, BlogArticleOverviewPresenter>(),
		BlogArticleOverviewMvp.View {

	@State
	lateinit var article: BlogArticles.Article

	override fun layout(): Int = R.layout.blog_article_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): BlogArticleOverviewPresenter = BlogArticleOverviewPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			article = intent!!.extras.getParcelable(BundleConstant.EXTRA)
			onInitViews(article)
		} else onInitViews(article)
		if (article.id.toIntOrNull() == null) {
			finish()
			return
		}
		title = article.title
		toolbar?.subtitle = getString(R.string.view_article)
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
						.appendEncodedPath("blogarticle${article.id}")
						.toString())
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onInitViews(article: BlogArticles.Article) {
		presenter.onGetArticle(article.id)

		userAvatar.setUrl(article.creation.user.avatar)
		articleUser.text = article.creation.user.login
		date.text = article.creation.date.parseFullDate(true).getTimeAgo()
		articleTitle.text = article.title

		comments.text = article.stats.commentCount ?: "0"
		likes.text = article.stats.likeCount ?: "0"

		if (isLoggedIn() && PrefGetter.getLoggedUser()?.id != article.creation.user.id.toInt()) {
			// TODO hardcoded isLiked
			val isLiked = false
			fab.setImageResource(R.drawable.ic_thumb_up)
			fab.isVisible = !isLiked
		} else {
			fab.visibility = View.GONE
		}

		fab.setOnClickListener { onFabClicked() }
	}

	private fun onFabClicked() {
		if (PrefGetter.getLoggedUser()?.id != article.creation.user.id.toInt()) {
			// TODO hardcoded likeType
			presenter.onSendVote(article.id.toInt(), true)
		}
	}

	override fun onInitArticleText(article: BlogArticle.Article) {
		hideProgress()
		articleText.html = article.text
		views.text = article.stats.viewCount ?: "0"
	}

	override fun onSetLike(likesCount: String, likeType: Boolean) {
		hideProgress()
		fab.isVisible = false
		likes.text = likesCount
	}

	companion object {
		fun startActivity(context: Context, article: BlogArticles.Article) {
			val intent = Intent(context, BlogArticleOverviewActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, article)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}
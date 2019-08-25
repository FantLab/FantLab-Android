package ru.fantlab.android.ui.modules.main.news.overview

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
import kotlinx.android.synthetic.main.news_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.main.news.contests.NewsContestsFragment
import androidx.core.util.Pair as UtilPair

class NewsOverviewActivity : BaseActivity<NewsOverviewMvp.View, NewsOverviewPresenter>(),
		NewsOverviewMvp.View {

	@State
	lateinit var news: News

	override fun layout(): Int = R.layout.news_layout

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = true

	override fun providePresenter(): NewsOverviewPresenter = NewsOverviewPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (savedInstanceState == null) {
			news = intent!!.extras.getParcelable(BundleConstant.EXTRA)
			onInitViews(news)
		} else onInitViews(news)
		if (news.id == -1) {
			finish()
			return
		}
		title = if (news.title.isEmpty()) getString(R.string.news) else news.title
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
						.appendEncodedPath("news${news.id}")
						.toString())
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onInitViews(news: News) {
		coverLayout.setUrl("https:${news.image}", "file:///android_asset/svg/fl_news.svg")

		if (!InputHelper.isEmpty(news.title)) {
			titleNews.text = news.title
			titleNews.visibility = View.VISIBLE
		} else titleNews.visibility = View.GONE

		if (!InputHelper.isEmpty(news.description) && !news.newsText.contains("print_contest")) {
			newsText.html = news.description
			newsText.visibility = View.VISIBLE
		} else if (!InputHelper.isEmpty(news.description) && news.newsText.contains("print_contest")) {
			newsText.html = news.newsText
			newsText.visibility = View.VISIBLE
		} else newsText.visibility = View.GONE


		loadContest(news)
	}

	private fun loadContest(news: News) {
		val pattern = "\\[print_contest=(\\d+)\\]".toRegex().find(news.newsText)
		val contestId = pattern?.groupValues?.get(1)
		if (contestId != null) {
			val fs = NewsContestsFragment.newInstance(contestId.toInt())
			supportFragmentManager
					.beginTransaction()
					.add(R.id.contestContainer, fs, NewsContestsFragment.TAG)
					.commit()
		}
	}

	companion object {
		fun startActivity(context: Context?, news: News) {
			val intent = Intent(context, NewsOverviewActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, news)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context?.startActivity(intent)
		}
	}
}

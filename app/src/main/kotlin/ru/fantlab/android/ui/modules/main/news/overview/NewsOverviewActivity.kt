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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.news_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.helper.*
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.main.news.contests.NewsContestsFragment
import android.support.v4.util.Pair as UtilPair
import com.bumptech.glide.request.target.Target

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
		Glide.with(this).load("https:${news.image}")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.listener(object : RequestListener<String, GlideDrawable> {
					override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
						coverLayout.visibility = View.GONE
						return false
					}
					override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
						return false
					}
				})
				.into(coverLayout)

		if (!InputHelper.isEmpty(news.title)) {
			titleNews.text = news.title
			titleNews.visibility = View.VISIBLE
		} else titleNews.visibility = View.GONE

		if (!InputHelper.isEmpty(news.description) && !news.newsText.contains("print_contest")) {
			newsText.html = news.description
			newsText.visibility = View.VISIBLE
		} else newsText.visibility = View.GONE

		date.text = news.newsDateIso.parseFullDate(true).getTimeAgo()
		author.html = news.author

		loadContest(news)
	}

	private fun loadContest(news: News) {
		val pattern = Regex("\\[print_contest=(\\d+)\\]")
		val contestId = pattern.matchEntire(news.newsText)?.groupValues?.get(1)
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

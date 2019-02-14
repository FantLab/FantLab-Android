package ru.fantlab.android.ui.modules.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*
import ru.fantlab.android.R
import ru.fantlab.android.provider.scheme.SchemeParser
import ru.fantlab.android.ui.base.BaseActivity

class AboutActivity : BaseActivity<AboutMvp.View, AboutPresenter>(), AboutMvp.View {

	override fun isTransparent(): Boolean = false
	override fun providePresenter(): AboutPresenter = AboutPresenter()
	override fun layout(): Int = R.layout.activity_about
	override fun canBack(): Boolean = true

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = getString(R.string.app_name)

		developersView.setOnClickListener(this)
		forumView.setOnClickListener(this)
		githubView.setOnClickListener(this)
		versionView.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.developersView -> toTelegram()
			R.id.forumView -> SchemeParser.openUrl(this, "https://fantlab.ru/forum/forum2page1/topic10144page1")
			R.id.githubView -> SchemeParser.openUrl(this, "https://github.com/FantLab/FantLab-Android")
			R.id.versionView -> {}
		}
	}

	private fun toTelegram() {
		val telegram = Intent(Intent.ACTION_VIEW)
		telegram.data =  Uri.parse("tg:resolve?domain=ilya_kokhan")
		startActivity(Intent.createChooser(telegram, getString(R.string.send_with)))
	}
}
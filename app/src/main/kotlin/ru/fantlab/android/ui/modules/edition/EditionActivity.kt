package ru.fantlab.android.ui.modules.edition

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.helper.ActivityHelper
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.edition.overview.EditionOverviewFragment

class EditionActivity : BaseActivity<EditionMvp.View, BasePresenter<EditionMvp.View>>(),
		EditionMvp.View {

	@State var editionId: Int = 0
	@State var editionName: String = ""

	override fun layout(): Int = R.layout.edition_layout

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = true

	override fun providePresenter(): BasePresenter<EditionMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hideShowShadow(true)
		if (savedInstanceState == null) {
			editionId = intent?.extras?.getInt(BundleConstant.EXTRA, -1) ?: -1
			editionName = intent?.extras?.getString(BundleConstant.EXTRA_TWO) ?: ""
		}
		if (editionId == -1) {
			finish()
			return
		}
		setTaskName(editionName)
		title = editionName

		if (savedInstanceState == null) {
			val editionFragment = EditionOverviewFragment.newInstance(editionId)
			supportFragmentManager
					.beginTransaction()
					.add(R.id.container, editionFragment, EditionOverviewFragment.TAG)
					.commit()
		}

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
						.appendPath("edition$editionId")
						.toString())
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {

		fun startActivity(context: Context, editionId: Int, editionName: String) {
			val intent = Intent(context, EditionActivity::class.java)
			intent.putExtras(Bundler.start()
					.put(BundleConstant.EXTRA, editionId)
					.put(BundleConstant.EXTRA_TWO, editionName)
					.end())
			if (context is Service || context is Application) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			context.startActivity(intent)
		}
	}
}
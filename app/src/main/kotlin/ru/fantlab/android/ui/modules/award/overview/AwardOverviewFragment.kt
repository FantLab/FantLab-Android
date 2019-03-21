package ru.fantlab.android.ui.modules.award.overview

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.award_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.glide.GlideApp
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.award.AwardPagerMvp


class AwardOverviewFragment : BaseFragment<AwardOverviewMvp.View, AwardOverviewPresenter>(),
		AwardOverviewMvp.View {

	private var pagerCallback: AwardPagerMvp.View? = null

	override fun fragmentLayout() = R.layout.award_overview_layout

	override fun providePresenter() = AwardOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(award: Award) {
		hideProgress()
		if (award.isOpened == 0) {
			showErrorMessage(getString(R.string.award_not_opened))
			activity?.finish()
			return
		}

		val awardLabel = if (!award.rusname.isEmpty()) {
			if (!award.name.isEmpty()) {
				String.format("%s / %s", award.rusname, award.name)
			} else {
				award.rusname
			}
		} else {
			award.name
		}

		pagerCallback?.onSetTitle(awardLabel)

		GlideApp.with(context!!)
				.load("https://${LinkParserHelper.HOST_DATA}/images/awards/${award.awardId}")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(coverLayout)

		if (award.rusname.isBlank()) {
			title.text = award.name
			title2.visibility = View.GONE
		} else {
			title.text = award.rusname
			if (award.name.isBlank()) {
				title2.visibility = View.GONE
			} else title2.text = award.name
		}

		if (!InputHelper.isEmpty(award.description))
			description.html = award.description
		else aboutView.visibility = View.GONE

		if (!InputHelper.isEmpty(award.comment))
			comment.html = award.comment
		else commentView.visibility = View.GONE

		if (!InputHelper.isEmpty(award.notes))
			notes.html = award.notes
		else notesView.visibility = View.GONE

		GlideApp.with(context!!)
				.load("https://${LinkParserHelper.HOST_DEFAULT}/img/flags/${award.countryId}.png")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(langIcon)

		country.text = award.countryName
		date.text = StringBuilder()
				.append(award.minDate.split("-")[0])
				.append(" - ")
				.append(award.maxDate.split("-")[0])
		homepage.html = award.homepage
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		progress.visibility = View.GONE
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun onShowErrorView(msgRes: String?) {
		parentView.visibility = View.GONE
		stateLayout.setEmptyText(R.string.network_error)
		stateLayout.showEmptyState()
	}


	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is AwardPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	override fun onSetTitle(title: String) {
		pagerCallback?.onSetTitle(title)
	}

	companion object {

		fun newInstance(awardId: Int): AwardOverviewFragment {
			val view = AwardOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, awardId).end()
			return view
		}
	}
}
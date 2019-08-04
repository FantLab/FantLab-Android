package ru.fantlab.android.ui.modules.profile.overview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat
import kotlinx.android.synthetic.main.profile_overview_icons_layout.*
import kotlinx.android.synthetic.main.profile_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.FantlabHelper.User.classRanges
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.modules.user.UserPagerMvp
import java.text.NumberFormat

class ProfileOverviewFragment : BaseFragment<ProfileOverviewMvp.View, ProfileOverviewPresenter>(),
		ProfileOverviewMvp.View {

	private val numberFormat = NumberFormat.getNumberInstance()

	override fun fragmentLayout(): Int = R.layout.profile_overview_layout

	override fun providePresenter(): ProfileOverviewPresenter = ProfileOverviewPresenter()

	private var pagerCallback: UserPagerMvp.View? = null

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		stateLayout.hideReload()
		presenter.onFragmentCreated(arguments!!)
	}

	override fun onInitViews(user: User) {
		progress.visibility = GONE
		val gender = if (user.sex == "m") /* male */ "\u2642" else /* female */ "\u2640"
		fio.text = if (user.fio.isEmpty()) {
			gender
		} else {
			StringBuilder()
					.append(user.fio)
					.append(" (")
					.append(gender)
					.append(")")
		}
		login.text = user.login
		avatarLayout.setUrl("https:${user.avatar}")
		val userLevel = numberFormat.format(user.level.toDouble())
		val maxClassLevel = numberFormat.format(classRanges[user.`class`].toLong())
		level.text = StringBuilder()
				.append(user.className)
				.append(", ")
				.append(if (user.`class` < 7) "$userLevel / $maxClassLevel" else userLevel)

		if (user.authorId == null && /*user.blogId == null*/true) {
			authorBlock.visibility = GONE
		} else {
			author.setOnClickListener {
				AuthorPagerActivity.startActivity(view!!.context, user.authorId ?: 0, user.authorName!!, 0)
			}
			if (/*user.blogId != null*/false /* раскомментировать, когда появится функционал блога */) {
				blog.setOnClickListener {
					// goto blog screen
					showMessage("Click", "Not implemented yet")
				}
			} else {
				divider.visibility = View.GONE
				blog.visibility = View.GONE
			}
		}

		marksLayout.setOnClickListener(this)
		marks.text = user.markCount.toString()
		responsesLayout.setOnClickListener(this)
		responses.text = user.responseCount.toString()
		votes.text = user.voteCount.toString()
		descriptions.text = user.descriptionCount.toString()
		classifications.text = user.classificationCount.toString()
		tickets.text = user.ticketCount.toString()
		messages.text = user.forumMessageCount.toString()
		topics.text = user.topicCount.toString()
		bookcases.text = user.bookcaseCount.toString()

		if (user.birthDay != null)
			birthDay.text = user.birthDay.parseFullDate().getTimeAgo()
		else birthDay.visibility = GONE

		 if (!user.countryName.isNullOrEmpty() && !user.cityName.isNullOrEmpty()) {
			 location.text = StringBuilder()
					 .append(user.countryName)
					 .append(", ")
					 .append(user.cityName)
		} else if (!user.location.isNullOrEmpty()) {
			 location.text = user.location
		} else {
			 location.visibility = GONE
		}
		regDate.text = user.regDate.parseFullDate().getTimeAgo()
		lastActionDate.text = user.lastActionDate.parseFullDate().getTimeAgo()
		if (user.sign.isNullOrEmpty()) {
			sign.visibility = View.GONE
		} else {
			sign.html = user.sign
		}
		if (user.blocked == 1) {
			block.text = if (user.blockEndDate != null) {
				String.format(
						"%s - %s",
						user.blockDate!!.parseFullDate().getTimeAgo(),
						user.blockEndDate.parseFullDate().getTimeAgo()
				)
			} else {
				getString(R.string.block_permanent)
			}
		} else {
			block.visibility = View.GONE
		}
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		progress.visibility = VISIBLE
	}

	override fun hideProgress() {
		progress.visibility = GONE
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun onShowErrorView(msgRes: String?) {
		parentView.visibility = View.GONE
		stateLayout.setEmptyText(R.string.network_error)
		stateLayout.showEmptyState()
		pagerCallback?.onError()
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.marksLayout -> {
				pagerCallback?.onSelectTab(1)
			}
			R.id.responsesLayout -> {
				pagerCallback?.onSelectTab(2)
			}
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is UserPagerMvp.View) {
			pagerCallback = context
		}
	}

	override fun onDetach() {
		pagerCallback = null
		super.onDetach()
	}

	companion object {

		fun newInstance(userId: Int): ProfileOverviewFragment {
			val view = ProfileOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
			return view
		}
	}
}
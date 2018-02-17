package ru.fantlab.android.ui.modules.profile

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.TooltipCompat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import butterknife.BindView
import es.dmoral.toasty.Toasty
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontButton
import ru.fantlab.android.ui.widgets.FontTextView

class ProfileOverviewFragment : BaseFragment<ProfileOverviewMvp.View, ProfileOverviewPresenter>(),
		ProfileOverviewMvp.View {

	@BindView(R.id.login) lateinit var login: FontTextView
	@BindView(R.id.fio) lateinit var fio: FontTextView
	@BindView(R.id.sign) lateinit var sign: FontTextView
	@BindView(R.id.avatarLayout) lateinit var avatarLayout: AvatarLayout
	@BindView(R.id.actionsLayout) lateinit var actionsLayout: ViewGroup
	@BindView(R.id.author) lateinit var author: FontButton
	@BindView(R.id.divider) lateinit var divider: View
	@BindView(R.id.blog) lateinit var blog: FontButton
	@BindView(R.id.marksLayout) lateinit var marksLayout: ViewGroup
	@BindView(R.id.marks) lateinit var marks: FontTextView
	@BindView(R.id.responsesLayout) lateinit var responsesLayout: ViewGroup
	@BindView(R.id.responses) lateinit var responses: FontTextView
	@BindView(R.id.votesLayout) lateinit var votesLayout: ViewGroup
	@BindView(R.id.votes) lateinit var votes: FontTextView
	@BindView(R.id.descriptionsLayout) lateinit var descriptionsLayout: ViewGroup
	@BindView(R.id.descriptions) lateinit var descriptions: FontTextView
	@BindView(R.id.classificationsLayout) lateinit var classificationsLayout: ViewGroup
	@BindView(R.id.classifications) lateinit var classifications: FontTextView
	@BindView(R.id.ticketsLayout) lateinit var ticketsLayout: ViewGroup
	@BindView(R.id.tickets) lateinit var tickets: FontTextView
	@BindView(R.id.messagesLayout) lateinit var messagesLayout: ViewGroup
	@BindView(R.id.messages) lateinit var messages: FontTextView
	@BindView(R.id.topicsLayout) lateinit var topicsLayout: ViewGroup
	@BindView(R.id.topics) lateinit var topics: FontTextView
	@BindView(R.id.bookcasesLayout) lateinit var bookcasesLayout: ViewGroup
	@BindView(R.id.bookcases) lateinit var bookcases: FontTextView
	@BindView(R.id.birthDay) lateinit var birthDay: FontTextView
	@BindView(R.id.location) lateinit var location: FontTextView
	@BindView(R.id.regDate) lateinit var regDate: FontTextView
	@BindView(R.id.lastActionDate) lateinit var lastActionDate: FontTextView
	@BindView(R.id.block) lateinit var block: FontTextView
	@BindView(R.id.progress) lateinit var progress: View
	var user: User? = null

	override fun fragmentLayout(): Int = R.layout.profile_overview_layout

	override fun providePresenter(): ProfileOverviewPresenter = ProfileOverviewPresenter()

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			presenter.onFragmentCreated(arguments)
		} else {
			user = savedInstanceState.getParcelable("user")
			if (user != null) {
				onInitViews(user!!)
			} else {
				presenter.onFragmentCreated(arguments)
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putParcelable("user", user)
	}

	override fun onInitViews(user: User?) {
		progress.visibility = GONE
		if (user == null) return
		this.user = user
		val gender = if (user.sex == "m") /* male */ "\u2642" else /* female */ "\u2640"
		fio.text = if (user.fio.isNullOrEmpty()) {
			gender
		} else {
			StringBuilder()
					.append(user.fio)
					.append(" (")
					.append(gender)
					.append(")")
		}
		login.text = user.login
		if (user.sign.isNullOrEmpty()) {
			sign.visibility = View.GONE
		} else {
			sign.text = user.sign
		}
		avatarLayout.setUrl("https://${user.avatar}")
		if (user.authorId != null) {
			author.setOnClickListener {
				// todo переход на экран автора
				Toasty.info(context!!, user.authorName ?: user.authorNameOrig ?: "").show()
			}
		} else {
			author.visibility = View.GONE
			divider.visibility = View.GONE
		}
		if (user.blogId != null) {
			blog.setOnClickListener {
				// todo переход в блог
				Toasty.info(context!!, user.blogId.toString()).show()
			}
		} else {
			divider.visibility = View.GONE
			blog.visibility = View.GONE
		}
		if (user.authorId == null && user.blogId == null) {
			actionsLayout.visibility = View.GONE
		}

		TooltipCompat.setTooltipText(marksLayout, getString(R.string.mark_count))
		marks.text = user.markCount.toString()
		TooltipCompat.setTooltipText(responsesLayout, getString(R.string.response_count))
		responses.text = user.responseCount.toString()
		TooltipCompat.setTooltipText(votesLayout, getString(R.string.votes_count))
		votes.text = user.voteCount.toString()
		TooltipCompat.setTooltipText(descriptionsLayout, getString(R.string.description_count))
		descriptions.text = user.descriptionCount.toString()
		TooltipCompat.setTooltipText(classificationsLayout, getString(R.string.classification_count))
		classifications.text = user.classifCount.toString()
		TooltipCompat.setTooltipText(ticketsLayout, getString(R.string.ticket_count))
		tickets.text = user.ticketsCount.toString()
		TooltipCompat.setTooltipText(messagesLayout, getString(R.string.forum_messages_count))
		messages.text = user.messageCount.toString()
		TooltipCompat.setTooltipText(topicsLayout, getString(R.string.topic_count))
		topics.text = user.topicCount.toString()
		TooltipCompat.setTooltipText(bookcasesLayout, getString(R.string.bookcase_count))
		bookcases.text = user.bookcaseCount.toString()

		birthDay.text = user.birthDay.getTimeAgo()
		location.text = if (!user.countryName.isNullOrEmpty() && !user.cityName.isNullOrEmpty()) {
			StringBuilder()
					.append(user.countryName)
					.append(", ")
					.append(user.cityName)
		} else if (!user.location.isNullOrEmpty()) {
			user.location
		} else {
			"N/A"
		}
		regDate.text = user.dateOfReg.getTimeAgo()
		lastActionDate.text = user.dateOfLastAction.getTimeAgo()
		if (user.block == 1) {
			block.text = if (user.dateOfBlockEnd != null) {
				String.format("%s - %s", user.dateOfBlock.getTimeAgo(), user.dateOfBlockEnd.getTimeAgo())
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

	override fun showErrorMessage(msgRes: String) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	companion object {

		fun newInstance(userId: Int): ProfileOverviewFragment {
			val view = ProfileOverviewFragment()
			view.arguments = Bundler.start().put(BundleConstant.EXTRA, userId).end()
			return view
		}
	}
}
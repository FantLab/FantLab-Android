package ru.fantlab.android.ui.widgets

import android.content.Context
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import ru.fantlab.android.R

/**
 * Created by Kosh on 20 Nov 2016, 12:21 AM
 */
open class StateLayout : NestedScrollView {

	@BindView(R.id.empty_text) lateinit var emptyText: FontTextView
	@BindView(R.id.reload) lateinit var reload: FontButton
	@State var layoutState = HIDDEN
	@State var emptyTextValue: String? = null
	@State var showReload = true
	private var onReloadListener: View.OnClickListener? = null

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	@OnClick(R.id.reload)
	fun onReload() {
		onReloadListener?.onClick(reload)
	}

	fun showProgress() {
		layoutState = SHOW_PROGRESS_STATE
		visibility = View.VISIBLE
		emptyText.visibility = View.GONE
		reload.visibility = View.GONE
	}

	fun hideProgress() {
		layoutState = HIDE_PROGRESS_STATE
		emptyText.visibility = View.VISIBLE
		reload.visibility = View.VISIBLE
		visibility = View.GONE
	}

	fun hideReload() {
		layoutState = HIDE_RELOAD_STATE
		reload.visibility = View.GONE
		emptyText.visibility = View.GONE
		visibility = View.GONE
	}

	fun showReload(adapterCount: Int) {
		showReload = adapterCount == 0
		showReload()
	}

	fun showReload() {
		hideProgress()
		if (showReload) {
			layoutState = SHOW_RELOAD_STATE
			reload.visibility = View.VISIBLE
			emptyText.visibility = View.VISIBLE
			visibility = View.VISIBLE
		}
	}

	fun setEmptyText(@StringRes resId: Int) {
		setEmptyText(resources.getString(resId))
	}

	fun setEmptyText(text: String?) {
		text?.let {
			this.emptyTextValue = "$it\n\n¯\\_(ツ)_/¯"
			emptyText.text = emptyTextValue
		}
	}

	fun showEmptyState() {
		hideProgress()
		hideReload()
		visibility = View.VISIBLE
		emptyText.visibility = View.VISIBLE
		layoutState = SHOW_EMPTY_STATE// last so it override visibility state.
	}

	fun setOnReloadListener(onReloadListener: View.OnClickListener) {
		this.onReloadListener = onReloadListener
	}

	override fun setVisibility(visibility: Int) {
		super.setVisibility(visibility)
		layoutState = if (visibility == View.GONE || visibility == View.INVISIBLE) {
			HIDDEN
		} else {
			SHOWN
		}
	}

	override fun onFinishInflate() {
		super.onFinishInflate()
		View.inflate(context, R.layout.empty_layout, this)
		if (isInEditMode) return
		ButterKnife.bind(this)
		emptyText.freezesText = true
	}

	override fun onDetachedFromWindow() {
		onReloadListener = null
		super.onDetachedFromWindow()
	}

	public override fun onSaveInstanceState(): Parcelable? {
		return StateSaver.saveInstanceState(this, super.onSaveInstanceState())
	}

	public override fun onRestoreInstanceState(state: Parcelable) {
		super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, state))
		onHandleLayoutState()
	}

	private fun onHandleLayoutState() {
		setEmptyText(emptyTextValue)
		when (layoutState) {
			SHOW_PROGRESS_STATE -> showProgress()
			HIDE_PROGRESS_STATE -> hideProgress()
			HIDE_RELOAD_STATE -> hideReload()
			SHOW_RELOAD_STATE -> showReload()
			HIDDEN -> visibility = View.GONE
			SHOW_EMPTY_STATE -> showEmptyState()
			SHOWN -> {
				visibility = View.VISIBLE
				showReload()
			}
		}
	}

	companion object {

		private const val SHOW_PROGRESS_STATE = 1
		private const val HIDE_PROGRESS_STATE = 2
		private const val HIDE_RELOAD_STATE = 3
		private const val SHOW_RELOAD_STATE = 4
		private const val SHOW_EMPTY_STATE = 7
		private const val HIDDEN = 5
		private const val SHOWN = 6
	}
}

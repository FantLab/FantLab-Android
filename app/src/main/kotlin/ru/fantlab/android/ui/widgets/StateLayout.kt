package ru.fantlab.android.ui.widgets

import android.content.Context
import android.os.Parcelable
import androidx.core.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import kotlinx.android.synthetic.main.empty_layout.view.*
import ru.fantlab.android.R

open class StateLayout : NestedScrollView {

	@State var layoutState = HIDDEN
	@State var emptyTextValue: String? = null
	@State var showReload = true
	private var onReloadListener: View.OnClickListener? = null

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


	fun onReload() {
		onReloadListener?.onClick(reload)
	}

	fun showProgress() {
		layoutState = SHOW_PROGRESS_STATE
		visibility = View.VISIBLE
		empty_text.visibility = View.GONE
		reload.visibility = View.GONE
	}

	fun hideProgress() {
		layoutState = HIDE_PROGRESS_STATE
		empty_text.visibility = View.VISIBLE
		reload.visibility = View.VISIBLE
		visibility = View.GONE
	}

	fun hideReload() {
		layoutState = HIDE_RELOAD_STATE
		reload.visibility = View.GONE
		empty_text.visibility = View.GONE
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
			empty_text.visibility = View.VISIBLE
			visibility = View.VISIBLE
		}
	}

	fun setEmptyText(@StringRes resId: Int) {
		setEmptyText(resources.getString(resId))
	}

	fun setEmptyText(text: String?) {
		text?.let {
			this.emptyTextValue = "$it\n\n¯\\_(ツ)_/¯"
			empty_text.text = emptyTextValue
		}
	}

	fun showEmptyState() {
		hideProgress()
		hideReload()
		visibility = View.VISIBLE
		empty_text.visibility = View.VISIBLE
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
		empty_text.freezesText = true
		reload.setOnClickListener { onReload() }
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

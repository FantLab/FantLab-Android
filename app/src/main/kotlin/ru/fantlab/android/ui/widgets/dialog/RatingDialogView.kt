package ru.fantlab.android.ui.widgets.dialog

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import kotlinx.android.synthetic.main.rate_dialog.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseBottomSheetDialog
import ru.fantlab.android.ui.widgets.ratingbar.BaseRatingBar

open class RatingDialogView : BaseBottomSheetDialog(), BaseRatingBar.OnRatingDoneListener {
	private var callback: RatingDialogViewActionCallback? = null
	var position: Int = -1
	var item: Any? = null

	interface RatingDialogViewActionCallback {
		fun onRated(rating: Float, listItem: Any, position: Int)
	}

	override fun layoutRes(): Int {
		return R.layout.rate_dialog
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		isViewportWidth = false
		if (parentFragment != null && parentFragment is RatingDialogViewActionCallback) {
			callback = parentFragment as RatingDialogViewActionCallback
		} else if (context is RatingDialogViewActionCallback) {
			callback = context
		}
	}

	override fun onDetach() {
		super.onDetach()
		callback = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val bundle = arguments ?: return
		val numStars = bundle.getInt(BundleConstant.EXTRA)
		val currentRate = bundle.getFloat(BundleConstant.EXTRA_TWO)
		val captionText = bundle.getString(BundleConstant.EXTRA_FOUR)
		position = bundle.getInt(BundleConstant.EXTRA_THREE)
		item = bundle.getParcelable(BundleConstant.ITEM)

		if (captionText.isNotEmpty()) {
			caption.text = captionText
			caption.visibility = View.VISIBLE
		}

		ratingBar.numStars = numStars
		ratingBar.rating = currentRate
		ratingBar.stepSize = 1f
		ratingBar.starPadding = 5
		ratingBar.isClickable = true
		ratingBar.isClearRatingEnabled = false
		ratingBar.setOnRatingDoneListener(this)

	}

	override fun onRatingDone(mRating: Float) {
		if (item != null) callback?.onRated(mRating, item!!, position)
		dismiss()
	}


	companion object {
		val TAG: String = RatingDialogView::class.java.simpleName

		fun newInstance(numStars: Int, currentRate: Float, listItem: Parcelable, caption: String, position: Int): RatingDialogView {
			val ratingDialogView = RatingDialogView()
			ratingDialogView.arguments = getBundle(numStars, currentRate, listItem, caption, position)
			return ratingDialogView
		}

		private fun getBundle(numStars: Int, currentRate: Float, listItem: Parcelable, caption: String, position: Int): Bundle {
			return Bundler.start()
					.put(BundleConstant.EXTRA, numStars)
					.put(BundleConstant.EXTRA_TWO, currentRate)
					.put(BundleConstant.EXTRA_THREE, position)
					.put(BundleConstant.EXTRA_FOUR, caption)
					.put(BundleConstant.ITEM, listItem)
					.end()
		}
	}
}
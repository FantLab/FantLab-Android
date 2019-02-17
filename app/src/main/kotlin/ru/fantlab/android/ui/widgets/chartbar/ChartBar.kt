package ru.fantlab.android.ui.widgets.chartbar

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.chartbar.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.base.BaseBottomSheetDialog

open class ChartBar : BaseBottomSheetDialog() {

	override fun layoutRes(): Int = R.layout.chartbar

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val bundle = arguments ?: return

		val title = bundle.getString(BundleConstant.EXTRA)
		val caption = bundle.getString(BundleConstant.EXTRA_TWO)
		val colored = bundle.getBoolean(BundleConstant.YES_NO_EXTRA)
		val points = bundle.getSerializable(BundleConstant.ITEM) as ArrayList<Pair<String, Int>>

		this.title.text = title

		if (caption.isNotEmpty()) {
			this.caption.text = caption
			this.caption.visibility = View.VISIBLE
		}

		renderChart(points, colored)
	}

	private fun renderChart(points: ArrayList<Pair<String, Int>>, colored: Boolean) {
		chartBarView.setPoints(points, colored)
	}

	companion object {
		val TAG: String = ChartBar::class.java.simpleName

		fun newInstance(title: String, caption: String = "", points: ArrayList<Pair<String, Int>>, colored: Boolean = false): ChartBar {
			val chartBarView = ChartBar()
			chartBarView.arguments = getBundle(title, caption, points, colored)
			return chartBarView
		}

		private fun getBundle(title: String, caption: String, points: ArrayList<Pair<String, Int>>, colored: Boolean): Bundle {
			return Bundler.start()
					.put(BundleConstant.EXTRA, title)
					.put(BundleConstant.EXTRA_TWO, caption)
					.put(BundleConstant.YES_NO_EXTRA, colored)
					.put(BundleConstant.ITEM, points)
					.end()
		}
	}
}
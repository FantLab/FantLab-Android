package ru.fantlab.android.ui.widgets

import android.app.Dialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SliderModel
import ru.fantlab.android.ui.adapter.SliderAdapter

class GallerySlider(context: Context?) : FrameLayout(context), View.OnTouchListener {
	private var dialog: Dialog = Dialog(context, R.style.SliderTheme)

	companion object {
		var mode: Int = 0
		var x1: Float = 0f
		var y1: Float = 0f
		var cY = 0f
		var SLIDE_NONE = 0
		var SLIDE_PRESSED = 1
		var SLIDE_HORIZONTAL = 2
		var SLIDE_VERTICAL = 3
		var hideSlider = false
	}

	var dotsViews = emptyArray<ImageView?>()
	var currentImageIndex: Int = 0

	fun showSlider(images: ArrayList<SliderModel>, startItem: Int) {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		dialog.setContentView(R.layout.slider_layout)
		dialog.setCanceledOnTouchOutside(false)
		val imagesCount = images.size
		val adapter = SliderAdapter(context!!, images)
		val pager = dialog.findViewById(R.id.viewPager) as ViewPager
		val sliderDots = dialog.findViewById(R.id.SliderDots) as LinearLayout
		pager.adapter = adapter
		pager.currentItem = startItem
		currentImageIndex = startItem
		pager.setOnTouchListener(this)
		dotsViews = arrayOfNulls(imagesCount)
		for (i in 0 until imagesCount) {
			dotsViews[i] = ImageView(context)
			dotsViews[i]?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.slider_nonactive_dot))
			val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
			params.setMargins(8, 8, 8, 8)
			sliderDots.addView(dotsViews[i], params)
		}
		pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(state: Int) {
			}

			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

			}

			override fun onPageSelected(position: Int) {
				dotsViews[currentImageIndex]?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider_nonactive_dot))
				dotsViews[position]?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider_active_dot))
				currentImageIndex = position
			}
		})
		dotsViews[startItem]?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.slider_active_dot))
		dialog.show()
	}

	private fun moveTo(v: View, y: Float, duration: Long) {
		v.animate()
				.y(y)
				.setDuration(duration)
				.start()
	}

	private fun setAlpha(v: View, a: Float) {
		v.alpha = a
	}

	override fun onTouch(view: View, e: MotionEvent): Boolean {
		val dx: Double
		val dy: Double
		when (e.action) {
			MotionEvent.ACTION_DOWN -> {
				mode = SLIDE_PRESSED
				x1 = e.x
				y1 = e.y
				cY = view.y - e.rawY
			}
			MotionEvent.ACTION_UP -> {
				mode = SLIDE_NONE
				if (hideSlider) {
					dialog.dismiss()
				} else {
					moveTo(view, 0f, 250)
					setAlpha(view, 1f)
				}
			}
			MotionEvent.ACTION_MOVE -> {
				dx = (e.x - x1).toDouble()
				dy = (e.y - y1).toDouble()
				when (mode) {
					SLIDE_PRESSED -> {
						val dis: Double = Math.sqrt(dx * dx + dy * dy)
						if (dis > 4) {
							mode = if (Math.abs(dx) > Math.abs(dy)) SLIDE_HORIZONTAL else SLIDE_VERTICAL
						}
					}
					SLIDE_HORIZONTAL -> {
						return false
					}
					SLIDE_VERTICAL -> {
						val yPosition = e.rawY + cY
						moveTo(view, yPosition, 0)
						hideSlider = Math.abs(yPosition) >= view.height / 2.7
						val movedTo = 1 - Math.abs(yPosition) / view.height * 2
						setAlpha(view, movedTo)
						return true
					}
				}
			}
		}
		return false
	}

}
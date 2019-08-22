package ru.fantlab.android.ui.widgets

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.image_layout.view.*
import ru.fantlab.android.R
import ru.fantlab.android.provider.glide.GlideApp
import ru.fantlab.android.provider.glide.SvgSoftwareLayerSetter

class CoverLayout : FrameLayout {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	private lateinit var progressBar: CircularProgressDrawable

	override fun onFinishInflate() {
		super.onFinishInflate()
		View.inflate(context, R.layout.image_layout, this)
		if (isInEditMode) return
		initProgressBar()

	}

	private fun initProgressBar() {
		progressBar = CircularProgressDrawable(context)
		progressBar.strokeWidth = 5f
		progressBar.centerRadius = 30f
		progressBar.start()
	}

	fun setUrl(url: String?, @DrawableRes fallbackImage: Int = R.drawable.work) {
		Glide.with(context)
				.load(url)
				.fallback(ContextCompat.getDrawable(context, fallbackImage))
				.placeholder(progressBar)
				.error(ContextCompat.getDrawable(context, fallbackImage))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(image)
	}

	fun setUrl(url: String?, defaultImageUrl: String) {
		GlideApp.with(context)
				.load(url)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.placeholder(progressBar)
				.error(GlideApp.with(context)
						.load(defaultImageUrl)
						.diskCacheStrategy(DiskCacheStrategy.DATA)
						.listener(SvgSoftwareLayerSetter())
				)
				.into(image)
	}
	
	fun setUrlGif(url: String?, @DrawableRes fallbackImage: Int = R.drawable.work) {
		image.scaleType = ImageView.ScaleType.FIT_START
		Glide.with(context)
				.load(url)
				.fallback(ContextCompat.getDrawable(context, fallbackImage))
				.error(ContextCompat.getDrawable(context, fallbackImage))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(image)
	}

	fun setDotColor(color: Dot.Color) {
		dot.apply {
			this.color = color
			visibility = View.VISIBLE
		}
	}
}

package ru.fantlab.android.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.image_layout.view.*
import ru.fantlab.android.R
import ru.fantlab.android.provider.glide.GlideApp

/**
 * Created by Kosh on 14 Nov 2016, 7:59 PM
 */

class AvatarLayout : FrameLayout {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	private lateinit var progressBar: CircularProgressDrawable

	override fun onFinishInflate() {
		super.onFinishInflate()
		View.inflate(context, R.layout.image_layout, this)
		if (isInEditMode) return
		image.setShape(ShapedImageView.SHAPE_MODE_CIRCLE, 0f)
		setBackgroundResource(R.drawable.circle_shape)
		initProgressBar()
	}

	private fun initProgressBar() {
		progressBar = CircularProgressDrawable(context)
		progressBar.strokeWidth = 5f
		progressBar.centerRadius = 30f
		progressBar.start()
	}

	fun setUrl(url: String?, @DrawableRes fallbackImage: Int = R.drawable.ic_person) {
		GlideApp.with(context)
				.load(url)
				.apply(RequestOptions.circleCropTransform())
				.placeholder(progressBar)
				.fallback(ContextCompat.getDrawable(context, fallbackImage))
				.error(ContextCompat.getDrawable(context, fallbackImage))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(image)
	}
}

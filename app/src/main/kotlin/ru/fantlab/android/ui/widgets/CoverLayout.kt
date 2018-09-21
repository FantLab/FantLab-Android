package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R

/**
 * Created by Kosh on 14 Nov 2016, 7:59 PM
 */

class CoverLayout : FrameLayout {

	@BindView(R.id.image) lateinit var cover: ShapedImageView
	@BindView(R.id.dot) lateinit var dot: Dot

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	override fun onFinishInflate() {
		super.onFinishInflate()
		View.inflate(context, R.layout.image_layout, this)
		if (isInEditMode) return
		ButterKnife.bind(this)
		cover.apply {
			setBackgroundResource(R.drawable.rect_shape)
			setShape(ShapedImageView.SHAPE_MODE_ROUND_RECT, 15f)
		}
	}

	fun setUrl(url: String?, @DrawableRes fallbackImage: Int = R.drawable.work) {
		Glide.with(context)
				.load(url)
				.fallback(ContextCompat.getDrawable(context, fallbackImage))
				.error(ContextCompat.getDrawable(context, fallbackImage))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(cover)
	}

	fun setDotColor(color: Dot.Color) {
		dot.apply {
			this.color = color
			visibility = View.VISIBLE
		}
	}
}
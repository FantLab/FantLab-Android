package ru.fantlab.android.provider.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget

class SvgSoftwareLayerSetter : RequestListener<Drawable> {
	override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
		val view = (target as ImageViewTarget<*>).view
		view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
		return false
	}

	override fun onResourceReady(resource: Drawable, model: Any,
								 target: com.bumptech.glide.request.target.Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
		val view = (target as ImageViewTarget<*>).view
		view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
		return false
	}
}
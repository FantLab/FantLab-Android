package ru.fantlab.android.ui.widgets.htmlview.drawable

import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import ru.fantlab.android.R
import java.lang.ref.WeakReference
import com.bumptech.glide.load.resource.gif.GifDrawable

internal class GlideDrawableTarget(private val urlDrawable: UrlDrawable, private val container: WeakReference<TextView>?, private val width: Int) : SimpleTarget<Drawable>() {

	override fun onResourceReady(resource: Drawable, glideAnimation: Transition<in Drawable>?) {
		if (container?.get() != null) {
			val textView = container.get() ?: return
			val width: Float
			val height: Float
			if (resource.intrinsicWidth < this.width) {
				val multiplier = 1.2.toFloat()
				width = resource.intrinsicWidth * multiplier
				height = resource.intrinsicHeight * multiplier
			} else {
				val downScale = resource.intrinsicWidth.toFloat() / this.width
				width = (resource.intrinsicWidth.toDouble() / downScale.toDouble() / 1.5).toFloat()
				height = (resource.intrinsicHeight.toDouble() / downScale.toDouble() / 1.5).toFloat()
			}
			val rect = Rect(0, 0, Math.round(width), Math.round(height))
			resource.bounds = rect
			urlDrawable.bounds = rect
			urlDrawable.drawable = resource
			if (resource is Animatable) {
				urlDrawable.callback = textView.getTag(R.id.drawable_callback) as Drawable.Callback
				if (resource is GifDrawable) resource.setLoopCount(GifDrawable.LOOP_FOREVER)
				resource.start()
			}
			textView.text = textView.text
			textView.invalidate()
		}
	}
}
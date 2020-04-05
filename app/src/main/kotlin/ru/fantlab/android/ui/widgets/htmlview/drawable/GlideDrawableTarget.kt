package ru.fantlab.android.ui.widgets.htmlview.drawable

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

internal open class GlideDrawableTarget(private val urlDrawable: UrlDrawable, private val container: WeakReference<TextView>?, private val width: Int) : CustomTarget<Drawable>() {

	override fun onResourceReady(resource: Drawable, glideAnimation: Transition<in Drawable>?) {
		if (container?.get() != null) {
			val textView = container.get() ?: return
			val width: Float = ((resource.intrinsicWidth / 1.3).toFloat());
			val height: Float = ((resource.intrinsicHeight / 1.3).toFloat());
			val rect = Rect(0, 0, width.roundToInt(), height.roundToInt())
			resource.bounds = rect
			urlDrawable.bounds = rect
			urlDrawable.drawable = resource
			textView.text = textView.text
			textView.invalidate()
		}
	}

	override fun onLoadCleared(placeholder: Drawable?) {}
}
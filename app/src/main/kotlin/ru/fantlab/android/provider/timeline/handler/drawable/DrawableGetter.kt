package ru.fantlab.android.provider.timeline.handler.drawable

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.fantlab.android.R
import java.lang.ref.WeakReference
import java.util.*

class DrawableGetter(tv: TextView, val width: Int) : Html.ImageGetter, Drawable.Callback {

	private var cachedTargets: MutableSet<GlideDrawableTarget>? = null
	private var container: WeakReference<TextView>? = null

	init {
		tv.setTag(R.id.drawable_callback, this)
		this.container = WeakReference(tv)
		this.cachedTargets = HashSet()
	}

	override fun getDrawable(url: String): Drawable {
		val urlDrawable = UrlDrawable()
		if (container != null && container?.get() != null) {
			val context = container?.get()!!.context
			val load = Glide.with(context)
					.load(url)
					.placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image))
					.dontAnimate()
			val target = GlideDrawableTarget(urlDrawable, container, width)
			load.into(target)
			cachedTargets?.add(target)
		}
		return urlDrawable
	}

	override fun invalidateDrawable(drawable: Drawable) {
		if (container != null && container?.get() != null) {
			container?.get()?.invalidate()
		}
	}

	override fun scheduleDrawable(drawable: Drawable, runnable: Runnable, l: Long) {}

	override fun unscheduleDrawable(drawable: Drawable, runnable: Runnable) {}

	fun clear(drawableGetter: DrawableGetter) {
		if (drawableGetter.cachedTargets != null) {
			for (target in drawableGetter.cachedTargets!!) {
				Glide.clear(target)
			}
		}
	}

}
package ru.fantlab.android.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.widget.RelativeLayout
import ru.fantlab.android.R

/**
 * An extension to [RelativeLayout] which has a foreground drawable.
 */
class ForegroundRelativeLayout @SuppressLint("CustomViewStyleable")
constructor(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

	private var foreground: Drawable? = null

	init {
		val a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView)
		val d = a.getDrawable(R.styleable.ForegroundView_android_foreground)
		if (d != null) {
			setForeground(d)
		}
		a.recycle()
		outlineProvider = ViewOutlineProvider.BOUNDS
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		foreground?.setBounds(0, 0, w, h)
	}

	override fun hasOverlappingRendering(): Boolean {
		return false
	}

	override fun verifyDrawable(who: Drawable): Boolean {
		return super.verifyDrawable(who) || who === foreground
	}

	override fun jumpDrawablesToCurrentState() {
		super.jumpDrawablesToCurrentState()
		foreground?.jumpToCurrentState()
	}

	override fun drawableStateChanged() {
		if (isInEditMode) return
		super.drawableStateChanged()
		foreground?.let {
			if (it.isStateful) {
				it.state = drawableState
			}
		}
	}

	override fun getForeground(): Drawable? {
		return foreground
	}

	override fun setForeground(drawable: Drawable) {
		if (foreground !== drawable) {
			foreground?.let {
				it.callback = null
				unscheduleDrawable(it)
			}

			foreground = drawable

			if (foreground != null) {
				foreground!!.let {
					it.setBounds(left, top, right, bottom)
					setWillNotDraw(false)
					it.callback = this
					if (it.isStateful) {
						it.state = drawableState
					}
				}
			} else {
				setWillNotDraw(true)
			}
			invalidate()
		}
	}

	override fun draw(canvas: Canvas) {
		super.draw(canvas)
		foreground?.draw(canvas)
	}

	override fun drawableHotspotChanged(x: Float, y: Float) {
		super.drawableHotspotChanged(x, y)
		foreground?.setHotspot(x, y)
	}
}
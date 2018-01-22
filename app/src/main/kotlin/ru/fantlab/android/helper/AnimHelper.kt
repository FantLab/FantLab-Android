package ru.fantlab.android.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.support.annotation.UiThread
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import java.util.*

object AnimHelper {

	private val interpolator = LinearInterpolator()

	@UiThread
	fun animateVisibility(view: View?, show: Boolean, visibility: Int = View.GONE) {
		if (view == null) {
			return
		}
		if (!ViewCompat.isAttachedToWindow(view)) {
			view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
				override fun onPreDraw(): Boolean {
					view.viewTreeObserver.removeOnPreDrawListener(this)
					animateSafeVisibility(show, view, visibility)
					return true
				}
			})
		} else {
			animateSafeVisibility(show, view, visibility)
		}
	}

	@UiThread
	private fun animateSafeVisibility(show: Boolean, view: View, visibility: Int) {
		view.animate().cancel()
		val animator = view.animate().setDuration(200).alpha(if (show) 1f else 0f).setInterpolator(AccelerateInterpolator())
				.setListener(object : AnimatorListenerAdapter() {
					override fun onAnimationStart(animation: Animator) {
						super.onAnimationStart(animation)
						if (show) {
							with(view) {
								scaleX = 1f
								scaleY = 1f
								this.visibility = View.VISIBLE
							}
						}
					}

					override fun onAnimationEnd(animation: Animator) {
						super.onAnimationEnd(animation)
						if (!show) {
							with(view) {
								this.visibility = visibility
								scaleX = 0f
								scaleY = 0f
							}
						}
						animation.removeListener(this)
						view.clearAnimation()
					}
				})
		animator.scaleX((if (show) 1 else 0).toFloat()).scaleY((if (show) 1 else 0).toFloat())
	}

	@UiThread
	fun revealDialog(dialog: Dialog, animDuration: Int) {
		if (dialog.window != null) {
			dialog.window?.decorView?.let {
				it.post {
					if (ViewCompat.isAttachedToWindow(it)) {
						val centerX = it.width / 2
						val centerY = it.height / 2
						val animator = ViewAnimationUtils.createCircularReveal(it, centerX, centerY, 20f, it.height.toFloat())
						animator.duration = animDuration.toLong()
						animator.start()
					}
				}
			}
		}
	}

	@UiThread
	fun dismissDialog(dialogFragment: DialogFragment, duration: Int, listenerAdapter: AnimatorListenerAdapter) {
		val dialog = dialogFragment.dialog
		if (dialog != null) {
			dialog.window?.decorView?.let {
				val centerX = it.width / 2
				val centerY = it.height / 2
				val radius = Math.sqrt((it.width * it.width / 4 + it.height * it.height / 4).toDouble()).toFloat()
				it.post {
					if (ViewCompat.isAttachedToWindow(it)) {
						val animator = ViewAnimationUtils.createCircularReveal(it, centerX, centerY, radius, 0f)
						animator.duration = duration.toLong()
						animator.addListener(listenerAdapter)
						animator.start()
					} else {
						listenerAdapter.onAnimationEnd(null)
					}
				}
			}
		} else {
			listenerAdapter.onAnimationEnd(null)
		}
	}

	@UiThread
	private fun getBeats(view: View): List<ObjectAnimator> {
		val animator = arrayOf<ObjectAnimator>(
				ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f),
				ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f)
		)
		return Arrays.asList(*animator)
	}

	@UiThread
	fun startBeatsAnimation(view: View) {
		view.clearAnimation()
		view.animation?.cancel()
		val animators = getBeats(view)
		for (anim in animators) {
			anim.setDuration(300).start()
			anim.interpolator = interpolator
		}
	}
}
package ru.fantlab.android.helper

import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.support.annotation.UiThread
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewCompat
import android.view.ViewAnimationUtils

object AnimHelper {

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
}
package org.odddev.fantlab.core.layers.router

import android.app.Activity
import android.support.annotation.IdRes

/**
 * @author kenrube
 * *
 * @since 28.09.16
 */

open class Router<out T : Activity>(protected val activity: T, @IdRes val containerId: Int)

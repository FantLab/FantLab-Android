package ru.fantlab.android.core.layers.router

import android.app.Activity
import android.support.annotation.IdRes

open class Router<out T : Activity>(protected val activity: T, @IdRes val containerId: Int)

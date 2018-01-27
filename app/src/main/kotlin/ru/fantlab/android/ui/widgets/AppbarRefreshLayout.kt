package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import ru.fantlab.android.R

/**
 * Created by kosh on 7/30/2015. CopyRights @
 */
class AppbarRefreshLayout(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs) {

	init {
		setColorSchemeResources(
				R.color.material_amber_700,
				R.color.material_blue_700,
				R.color.material_purple_700,
				R.color.material_lime_700
		)
	}
}

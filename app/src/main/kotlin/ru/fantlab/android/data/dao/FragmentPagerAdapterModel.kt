package ru.fantlab.android.data.dao

import android.content.Context
import android.support.v4.app.Fragment
import ru.fantlab.android.R
import ru.fantlab.android.ui.modules.main.responses.ResponsesFragment

data class FragmentPagerAdapterModel(
		val title: String,
		val fragment: Fragment,
		val key: String? = null
) {

	companion object {

		fun buildForSearch(context: Context): MutableList<FragmentPagerAdapterModel> {
			return mutableListOf(
					FragmentPagerAdapterModel(context.getString(R.string.authors), /*SearchAuthorsFragment()*/ResponsesFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.works), /*SearchWorksFragment()*/ResponsesFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.editions), /*SearchEditionsFragment()*/ResponsesFragment()),
					FragmentPagerAdapterModel(context.getString(R.string.awards), /*SearchAwardsFragment()*/ResponsesFragment())
			)
		}
	}
}
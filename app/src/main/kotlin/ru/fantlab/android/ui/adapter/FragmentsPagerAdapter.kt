package ru.fantlab.android.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel

/**
 * Created by Kosh on 03 Dec 2016, 9:25 AM
 */

class FragmentsPagerAdapter(fm: FragmentManager, private val fragments: MutableList<FragmentPagerAdapterModel>)
	: FragmentStatePagerAdapter(fm) {

	override fun getItem(position: Int): Fragment {
		return fragments[position].fragment
	}

	override fun getCount(): Int {
		return fragments.size
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return fragments[position].title
	}

	fun remove(model: FragmentPagerAdapterModel) {
		fragments.remove(model)
		notifyDataSetChanged()
	}

	fun remove(position: Int) {
		fragments.removeAt(position)
		notifyDataSetChanged()
	}
}

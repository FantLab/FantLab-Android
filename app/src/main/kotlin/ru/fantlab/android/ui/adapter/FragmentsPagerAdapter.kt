package ru.fantlab.android.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel

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

	fun getCustomTabView(context: Context): View {
		return LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
	}

	fun getItemKey(position: Int): String? {
		return fragments[position].key
	}
}

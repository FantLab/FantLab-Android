package ru.fantlab.android.helper

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object AppHelper {

	fun getFragmentByTag(fragmentManager: FragmentManager, tag: String): Fragment = fragmentManager.findFragmentByTag(tag)

	fun isNightMode(resources: Resources): Boolean = PrefGetter.getThemeType(resources) != PrefGetter.ThemeType.LIGHT
}
package ru.fantlab.android.ui.modules.theme.fragment

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.theme_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.PrefHelper
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.base.BaseFragment

class ThemeFragment : BaseFragment<ThemeFragmentMvp.View, ThemeFragmentPresenter>(), ThemeFragmentMvp.View {

	private val THEME = "appTheme"
	private var primaryDarkColor: Int = 0
	private var theme: Int = 0
	private var themeListener: ThemeFragmentMvp.ThemeListener? = null

	override fun onAttach(context: Context) {
		super.onAttach(context)
		themeListener = context as ThemeFragmentMvp.ThemeListener
	}

	override fun onDetach() {
		themeListener = null
		super.onDetach()
	}

	override fun fragmentLayout(): Int = 0

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		applyButton.setOnClickListener {
			setTheme()
		}
		toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		theme = arguments!!.getInt(BundleConstant.ITEM)
		val contextThemeWrapper = ContextThemeWrapper(activity, theme)
		primaryDarkColor = ViewHelper.getPrimaryDarkColor(contextThemeWrapper)
		val localInflater = inflater.cloneInContext(contextThemeWrapper)
		return localInflater.inflate(R.layout.theme_layout, container, false)!!
	}

	override fun providePresenter(): ThemeFragmentPresenter {
		return ThemeFragmentPresenter()
	}

	override fun setUserVisibleHint(isVisibleToUser: Boolean) {
		super.setUserVisibleHint(isVisibleToUser)
		if (isVisibleToUser) {
			if (themeListener != null) {
				themeListener!!.onChangePrimaryDarkColor(primaryDarkColor, theme == R.style.ThemeLight)
			}
		}
	}

	companion object {
		fun newInstance(style: Int): ThemeFragment {
			val fragment = ThemeFragment()
			fragment.arguments = Bundler.start()
					.put(BundleConstant.ITEM, style)
					.end()
			return fragment
		}
	}

	private fun setTheme() {
		when (theme) {
			R.style.ThemeLight -> setTheme(getString(R.string.light_theme_mode))
			R.style.ThemeDark -> setTheme(getString(R.string.dark_theme_mode))
			R.style.ThemeAmlod -> setTheme(getString(R.string.amlod_theme_mode))
			R.style.ThemeBluish -> setTheme(getString(R.string.bluish_theme))
			R.style.ThemeMidnight -> setTheme(getString(R.string.mid_night_blue_theme_mode))
		}
	}

	private fun setTheme(theme: String) {
		PrefHelper[THEME] = theme
		themeListener?.onThemeApplied()
	}

}

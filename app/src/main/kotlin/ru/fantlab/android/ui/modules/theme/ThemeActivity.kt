package ru.fantlab.android.ui.modules.theme

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import kotlinx.android.synthetic.main.theme_viewpager.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.FragmentPagerAdapterModel
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.ui.adapter.FragmentsPagerAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.modules.theme.fragment.ThemeFragmentMvp
import ru.fantlab.android.ui.widgets.CardsPagerTransformerBasic
import ru.fantlab.android.ui.widgets.ViewPagerView


class ThemeActivity : BaseActivity<BaseMvp.View, BasePresenter<BaseMvp.View>>(), ThemeFragmentMvp.ThemeListener {

	override fun layout(): Int = R.layout.theme_viewpager

	override fun isTransparent(): Boolean = false

	override fun canBack(): Boolean = false

	override fun providePresenter(): BasePresenter<BaseMvp.View> = BasePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		pager.adapter = FragmentsPagerAdapter(supportFragmentManager, FragmentPagerAdapterModel.buildForTheme())
		pager.clipToPadding = false
		val partialWidth = resources.getDimensionPixelSize(R.dimen.spacing_s_large)
		val pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
		val pagerPadding = partialWidth + pageMargin
		pager.pageMargin = pageMargin
		pager.setPageTransformer(true, CardsPagerTransformerBasic(4, 10))
		pager.setPadding(pagerPadding, pagerPadding, pagerPadding, pagerPadding)
		if (savedInstanceState == null) {
			val theme = PrefGetter.getThemeType(this)
			pager.setCurrentItem(theme.ordinal, true)
		}
	}

	override fun onChangePrimaryDarkColor(color: Int, darkIcons: Boolean) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val view = window.decorView
			view.systemUiVisibility = if (darkIcons) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else view.systemUiVisibility and View
					.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
		}
		val cx = parentLayout.width / 2
		val cy = parentLayout.height / 2
		if (parentLayout.isAttachedToWindow) {
			val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
			val anim = ViewAnimationUtils.createCircularReveal(parentLayout, cx, cy, 0f, finalRadius)
			anim.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator?) {
					super.onAnimationEnd(animation)
					window?.statusBarColor = color
					changeNavColor(color)
				}
			})
			parentLayout.setBackgroundColor(color)
			anim.start()
		} else {
			parentLayout.setBackgroundColor(color)
			window.statusBarColor = color
			changeNavColor(color)
		}
	}

	private fun changeNavColor(color: Int) {
		window?.navigationBarColor = color
	}

	override fun onThemeApplied() {
		showMessage(R.string.success, R.string.change_theme_warning)
		onThemeChanged()
	}
}
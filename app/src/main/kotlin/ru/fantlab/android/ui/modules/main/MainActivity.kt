package ru.fantlab.android.ui.modules.main

import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseActivity

class MainActivity : BaseActivity<MainMvp.View, MainPresenter>(), MainMvp.View {

	override fun layout(): Int = R.layout.activity_main_view

	override fun isTransparent(): Boolean = true

	override fun canBack(): Boolean = false

	override fun isSecured(): Boolean = false

	override fun providePresenter(): MainPresenter = MainPresenter()
}
package ru.fantlab.android.ui.modules.main

import ru.fantlab.android.ui.base.BaseActivity

class MainActivity : BaseActivity<MainMvp.View, MainPresenter>(), MainMvp.View {

	override fun layout(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isTransparent(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun canBack(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isSecured(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun providePresenter(): MainPresenter = MainPresenter()
}
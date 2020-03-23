package ru.fantlab.android.ui.modules.translator.overview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.translator_overview_layout.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.translator.TranslatorMvp

class TranslatorOverviewFragment : BaseFragment<TranslatorOverviewMvp.View, TranslatorOverviewPresenter>(),
        TranslatorOverviewMvp.View {

    private var pagerCallback: TranslatorMvp.View? = null

    override fun fragmentLayout() = R.layout.translator_overview_layout

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        stateLayout.hideReload()
        presenter.onFragmentCreated(arguments!!)
    }

    override fun providePresenter() = TranslatorOverviewPresenter()

    override fun onTranslatorInformationRetrieved(translator: Translator) {
        hideProgress()
        coverLayouts.setUrl("https:${translator.image}")

        if (!translator.countries.isEmpty()) {
            translatorCountryInfo.text = translator.countries[0].name
            translatorCountryInfoBlock.visibility = View.VISIBLE
        } else translatorCountryInfoBlock.visibility = View.GONE

        if (InputHelper.isEmpty(translator.name)) {
            translatorName.text = translator.nameOriginal
            translatorNameOrig.visibility = View.GONE
        } else {
            translatorName.text = translator.name
            if (!InputHelper.isEmpty(translator.nameOriginal))
                translatorNameOrig.text = translator.nameOriginal
            else
                translatorNameOrig.visibility = View.GONE
        }

        pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(translator.name)) translator.name else translator.nameOriginal)

        if (!translator.birthday.isNullOrEmpty()) {
            translatorBorn.text = translator.birthday
            translatorBornBlock.visibility = View.VISIBLE
        } else translatorBornBlock.visibility = View.GONE

        if (!translator.deathday.isNullOrEmpty()) {
            translatorDie.text = translator.deathday
            translatorDieBlock.visibility = View.VISIBLE
        } else translatorDieBlock.visibility = View.GONE

        if (!translator.translatorFrom.isNullOrEmpty()) {
            translatorFrom.text = translator.translatorFrom
            translatorFromBlock.visibility = View.VISIBLE
        } else translatorFromBlock.visibility = View.GONE

        if (!translator.translatorTo.isNullOrEmpty()) {
            translatorTo.text = translator.translatorTo
            translatorToBlock.visibility = View.VISIBLE
        } else translatorToBlock.visibility = View.GONE

        if (!translator.biography.isNullOrEmpty()) {
            val source = when {
                translator.biographySource.isNotEmpty() -> {
                    getString(R.string.copyright, translator.biographySource)
                }
                else -> {
                    ""
                }
            }

            biographyText.html = translator.biography.replace("(\r\n)+".toRegex(), "\n").trim() + "\n\n" + source

            biographyBlock.visibility = View.VISIBLE
        } else biographyBlock.visibility = View.GONE

    }

    override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showErrorMessage(msgRes: String?) {
        hideProgress()
        super.showErrorMessage(msgRes)
    }

    override fun onShowErrorView(msgRes: String?) {
        parentView.visibility = View.GONE
        stateLayout.setEmptyText(R.string.network_error)
        stateLayout.showEmptyState()
    }

    override fun showMessage(titleRes: Int, msgRes: Int) {
        hideProgress()
        super.showMessage(titleRes, msgRes)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TranslatorMvp.View) {
            pagerCallback = context
        }
    }

    override fun onDetach() {
        pagerCallback = null
        super.onDetach()
    }

    companion object {

        fun newInstance(translatorId: Int): TranslatorOverviewFragment {
            val view = TranslatorOverviewFragment()
            view.arguments = Bundler.start().put(BundleConstant.EXTRA, translatorId).end()
            return view
        }
    }
}
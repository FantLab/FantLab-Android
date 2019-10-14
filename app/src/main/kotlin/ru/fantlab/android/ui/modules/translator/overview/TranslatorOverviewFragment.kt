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

        /*if (!InputHelper.isEmpty(author.countryName)) {
            authorCountryInfo.text = author.countryName
            authorCountryInfoBlock.visibility = View.VISIBLE
        } else authorCountryInfoBlock.visibility = View.GONE

        if (InputHelper.isEmpty(author.name)) {
            authorName.text = author.nameOriginal
            authorNameOrig.visibility = View.GONE
        } else {
            authorName.text = author.name
            if (!InputHelper.isEmpty(author.nameOriginal))
                authorNameOrig.text = author.nameOriginal
            else
                authorNameOrig.visibility = View.GONE
        }*/

        pagerCallback?.onSetTitle(if (!InputHelper.isEmpty(translator.name)) translator.name else translator.nameOriginal)

        /*if (!author.birthDay.isNullOrEmpty()) {
            editionBorn.text = author.birthDay
            editionBornBlock.visibility = View.VISIBLE
        } else editionBornBlock.visibility = View.GONE

        if (!author.deathDay.isNullOrEmpty()) {
            editionDie.text = author.deathDay
            editionDieBlock.visibility = View.VISIBLE
        } else editionDieBlock.visibility = View.GONE*/


        if (!translator?.biography.isNullOrEmpty()) {
            /*val source = when {
                biography!!.source.isNotEmpty() && biography.sourceLink.isNotEmpty() -> {
                    val sourceText = "© <a href=\"${biography.sourceLink}\">${biography.source}</a>"
                    sourceText
                }
                biography.source.isNotEmpty() -> {
                    getString(R.string.copyright, biography.source)
                }
                biography.sourceLink.isNotEmpty() -> {
                    getString(R.string.copyright, biography.sourceLink)
                }
                else -> {
                    ""
                }
            }

            biographyText.html = biography.biography.replace("(\r\n)+".toRegex(), "\n").trim() + "\n\n" + source*/

            biographyText.html = translator.biography!!.replace("(\r\n)+".toRegex(), "\n").trim()

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
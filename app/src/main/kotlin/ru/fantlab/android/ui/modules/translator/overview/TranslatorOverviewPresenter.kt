package ru.fantlab.android.ui.modules.translator.overview

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.data.dao.response.TranslatorResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getTranslatorInformationPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class TranslatorOverviewPresenter : BasePresenter<TranslatorOverviewMvp.View>(), TranslatorOverviewMvp.Presenter {

    override fun onFragmentCreated(bundle: Bundle) {
        val translatorId = bundle.getInt(BundleConstant.EXTRA)
        makeRestCall(
            retrieveTranslatorInfoInternal(translatorId).toObservable(),
            Consumer { (translator, awards) ->
                sendToView { it.onTranslatorInformationRetrieved(translator, awards) }
            }
        )
    }

    override fun onItemClick(position: Int, v: View?, item: Nomination) {
        sendToView { it.onItemClicked(item) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: Nomination) {
    }

    private fun retrieveTranslatorInfoInternal(translatorId: Int) =
            retrieveTranslatorInfoFromServer(translatorId)
                    .onErrorResumeNext { retrieveTranslatorInfoFromDb(translatorId) }

    private fun retrieveTranslatorInfoFromServer(translatorId: Int): Single<Pair<Translator, ArrayList<Translator.TranslationAward>>> =
            DataManager.getTranslatorInformation(translatorId, showBio = true, showAwards = true)
                    .map { getTranslator(it) }

    private fun retrieveTranslatorInfoFromDb(translatorId: Int): Single<Pair<Translator, ArrayList<Translator.TranslationAward>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getTranslatorInformationPath(translatorId, showBio = true, showAwards = true))
                    .map { it.response }
                    .map { TranslatorResponse.Deserializer().deserialize(it) }
                    .map { getTranslator(it) }

    private fun getTranslator(response: TranslatorResponse): Pair<Translator, ArrayList<Translator.TranslationAward>> =
            Pair(response.translator, response.awards)

}
package ru.fantlab.android.ui.modules.translator.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
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
            Consumer { translator ->
                sendToView { it.onTranslatorInformationRetrieved(translator) }
            }
        )
    }

    private fun retrieveTranslatorInfoInternal(translatorId: Int) =
            retrieveTranslatorInfoFromServer(translatorId)
                    .onErrorResumeNext { retrieveTranslatorInfoFromDb(translatorId) }

    private fun retrieveTranslatorInfoFromServer(translatorId: Int): Single<Translator> =
            DataManager.getTranslatorInformation(translatorId, showBio = true)
                    .map { getTranslator(it) }

    private fun retrieveTranslatorInfoFromDb(translatorId: Int): Single<Translator> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getTranslatorInformationPath(translatorId))
                    .map { it.response }
                    .map { TranslatorResponse.Deserializer().deserialize(it) }
                    .map { getTranslator(it) }

    private fun getTranslator(response: TranslatorResponse): Translator =
            response.translator

}
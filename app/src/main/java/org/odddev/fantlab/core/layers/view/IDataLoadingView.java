package org.odddev.fantlab.core.layers.view;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public interface IDataLoadingView<D> extends IView {

    void showData(D data);

    void showError(String errorMessage);

    void showLoading();
}

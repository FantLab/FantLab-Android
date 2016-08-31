package org.odddev.fantlab.core.layers.presenter;

import org.odddev.fantlab.core.layers.view.IFormView;
import org.odddev.fantlab.core.validator.Validator;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public abstract class DataFormPresenter<M, E extends Validator, V extends IFormView<M, E>>
        extends Presenter<V> {

    protected M mData;

    public abstract void checkForm(M data);

    protected void showValidator(E error) {
        for (V dataView : getViews()) {
            dataView.showValidator(error);
        }
    }

    protected void showError(String errorMessage) {
        for (V dataView : getViews()) {
            dataView.showError(errorMessage);
        }
    }

    protected void showError(int errorResource) {
        for (V dataView : getViews()) {
            dataView.showError(errorResource);
        }
    }

    protected void success() {
        for (V dataView : getViews()) {
            dataView.success();
        }
    }
}

package org.odddev.fantlab.core.layers.view;

import org.odddev.fantlab.core.validator.Validator;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public interface IFormView<M, E extends Validator> extends IDataLoadingView<M> {

    void showValidator(E validator);

    void showError(int errorResource);

    void success();
}

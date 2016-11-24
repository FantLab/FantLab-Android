package org.odddev.fantlab.auth.reg;

import org.odddev.fantlab.core.layers.view.IView;

/**
 * @author kenrube
 * @since 18.09.16
 */

public interface IRegView extends IView {

    void showRegResult(boolean registered);

    void showError(String error);
}

package org.odddev.fantlab.auth.login;

import org.odddev.fantlab.core.layers.view.IView;

/**
 * @author kenrube
 * @since 30.08.16
 */

public interface ILoginView extends IView {

    void showLoginResult(boolean loggedIn);

    void showError(String error);
}

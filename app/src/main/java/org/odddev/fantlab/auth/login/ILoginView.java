package org.odddev.fantlab.auth.login;

import com.arellomobile.mvp.MvpView;

/**
 * @author kenrube
 * @since 30.08.16
 */

interface ILoginView extends MvpView {

    void showLoginResult(boolean loggedIn);

    void showError(String error);

    void showFieldsInvalid();
}

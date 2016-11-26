package org.odddev.fantlab.auth.reg;

import com.arellomobile.mvp.MvpView;

/**
 * @author kenrube
 * @since 18.09.16
 */

interface IRegView extends MvpView {

    void showRegResult(boolean registered);

    void showError(String error);
}

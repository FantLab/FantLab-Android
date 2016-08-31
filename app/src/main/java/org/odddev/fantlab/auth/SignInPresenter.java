package org.odddev.fantlab.auth;

import org.odddev.fantlab.core.layers.presenter.DataFormPresenter;
import org.odddev.fantlab.profile.ProfileValidator;
import org.odddev.fantlab.profile.User;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public class SignInPresenter extends DataFormPresenter<User, ProfileValidator, ISignInView> {

    public SignInPresenter() {

    }

    @Override
    public void checkForm(User data) {

    }
}

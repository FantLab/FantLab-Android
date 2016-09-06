package org.odddev.fantlab.auth;

import org.odddev.fantlab.core.layers.presenter.DataFormPresenter;
import org.odddev.fantlab.core.rx.ConfiguratorProvider;
import org.odddev.fantlab.core.validator.ValidatorException;
import org.odddev.fantlab.profile.ProfileValidator;
import org.odddev.fantlab.profile.User;

import javax.inject.Inject;

import rx.Observable;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public class SignInPresenter extends DataFormPresenter<User, ProfileValidator, ISignInView> {

    @Inject
    ConfiguratorProvider mConfiguratorProvider;

    public SignInPresenter() {
    }

    @Override
    public void checkForm(User user) {
        ProfileValidator validator = ProfileValidator.newInstance(user, ProfileValidator.TYPE_LOGIN);
        validator
                .getValidatorObservable()
                .compose(mConfiguratorProvider.applySchedulers())
                .flatMap(profileValidator -> {
                    showValidator(profileValidator);
                    if (profileValidator.formIsValid()) {
                        return mUserProvider.login(user);
                    }
                    return Observable.error(new ValidatorException());
                });
    }
}

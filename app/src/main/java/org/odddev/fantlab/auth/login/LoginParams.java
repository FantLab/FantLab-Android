package org.odddev.fantlab.auth.login;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.StringRes;
import android.text.Editable;

import org.odddev.fantlab.BR;
import org.odddev.fantlab.R;
import org.odddev.fantlab.core.utils.AbsTextWatcher;

/**
 * @author kenrube
 * @date 17.09.16
 */

public class LoginParams extends BaseObservable {

    private enum Field {
        LOGIN(R.string.error_username_empty),
        PASSWORD(R.string.error_password_empty);

        @StringRes
        private int mTextResource;

        Field(@StringRes int textResource) {
            this.mTextResource = textResource;
        }

        @StringRes
        public int getResource() {
            return mTextResource;
        }
    }

    private Resources mResources;

    public LoginTextWatcher loginWatcher;
    public LoginTextWatcher passwordWatcher;

    public String login = "";
    public String password = "";

    private String mLoginError = null;
    private String mPasswordError = null;

    LoginParams(Context context) {
        mResources = context.getResources();
        loginWatcher = new LoginTextWatcher(Field.LOGIN);
        passwordWatcher = new LoginTextWatcher(Field.PASSWORD);
    }

    private void validate(Field field) {
        switch (field) {
            case LOGIN: {
                setLoginError(login.isEmpty()
                        ? mResources.getString(field.getResource())
                        : null);
                break;
            }
            case PASSWORD: {
                setPasswordError(password.isEmpty()
                        ? mResources.getString(field.getResource())
                        : null);
                break;
            }
        }
    }

    boolean isValid() {
        for (Field field : Field.values()) {
            validate(field);
        }
        return mLoginError == null && mPasswordError == null;
    }

    @Bindable
    public String getLoginError() {
        return mLoginError;
    }

    private void setLoginError(String loginError) {
        this.mLoginError = loginError;
        notifyPropertyChanged(BR.loginError);
    }

    @Bindable
    public String getPasswordError() {
        return mPasswordError;
    }

    private void setPasswordError(String passwordError) {
        this.mPasswordError = passwordError;
        notifyPropertyChanged(BR.passwordError);
    }

    public class LoginTextWatcher extends AbsTextWatcher {

        private Field mField;

        LoginTextWatcher(Field field) {
            mField = field;
        }

        @Override
        public void afterTextChanged(Editable s) {
            validate(mField);
        }
    }
}

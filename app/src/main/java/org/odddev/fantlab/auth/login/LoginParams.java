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
        USERNAME(R.string.error_username_empty),
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

    public LoginTextWatcher usernameWatcher;
    public LoginTextWatcher passwordWatcher;

    public String username = "";
    public String password = "";

    private String mUsernameError = null;
    private String mPasswordError = null;

    LoginParams(Context context) {
        mResources = context.getResources();
        usernameWatcher = new LoginTextWatcher(Field.USERNAME);
        passwordWatcher = new LoginTextWatcher(Field.PASSWORD);
    }

    private void validate(Field field) {
        switch (field) {
            case USERNAME: {
                setUsernameError(username.isEmpty()
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
        return mUsernameError == null && mPasswordError == null;
    }

    @Bindable
    public String getUsernameError() {
        return mUsernameError;
    }

    private void setUsernameError(String usernameError) {
        this.mUsernameError = usernameError;
        notifyPropertyChanged(BR.usernameError);
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

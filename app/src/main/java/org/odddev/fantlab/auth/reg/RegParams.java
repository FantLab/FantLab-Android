package org.odddev.fantlab.auth.reg;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.util.Patterns;

import org.odddev.fantlab.BR;
import org.odddev.fantlab.R;
import org.odddev.fantlab.core.utils.AbsTextWatcher;

/**
 * @author kenrube
 * @date 18.09.16
 */

public class RegParams extends BaseObservable {

    private enum Field {
        LOGIN(R.string.error_username_empty),
        PASSWORD(R.string.error_password_empty),
        EMAIL(R.string.error_email_incorrect);

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

    public RegTextWatcher loginWatcher;
    public RegTextWatcher passwordWatcher;
    public RegTextWatcher emailWatcher;

    public String login = "";
    public String password = "";
    public String email = "";

    private String mLoginError = null;
    private String mPasswordError = null;
    private String mEmailError = null;

    RegParams(Context context) {
        mResources = context.getResources();
        loginWatcher = new RegTextWatcher(Field.LOGIN);
        passwordWatcher = new RegTextWatcher(Field.PASSWORD);
        emailWatcher = new RegTextWatcher(Field.EMAIL);
    }

    private void validate(Field field) {
        switch (field) {
            case LOGIN: {
                setLoginError(!login.isEmpty()
                        ? null
                        : mResources.getString(field.getResource()));
                break;
            }
            case PASSWORD: {
                setPasswordError(!password.isEmpty()
                        ? null
                        : mResources.getString(field.getResource()));
                break;
            }
            case EMAIL: {
                setEmailError(isEmailCorrect()
                        ? null
                        : mResources.getString(field.getResource()));
                break;
            }
        }
    }

    boolean isValid() {
        for (Field field : Field.values()) {
            validate(field);
        }
        return mLoginError == null && mPasswordError == null && mEmailError == null;
    }

    private boolean isEmailCorrect() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    @Bindable
    public String getEmailError() {
        return mEmailError;
    }

    private void setEmailError(String emailError) {
        this.mEmailError = emailError;
        notifyPropertyChanged(BR.emailError);
    }

    public class RegTextWatcher extends AbsTextWatcher {
        private RegParams.Field mField;

        RegTextWatcher(RegParams.Field field) {
            mField = field;
        }

        @Override
        public void afterTextChanged(Editable s) {
            validate(mField);
        }
    }
}

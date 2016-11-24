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
 * @since 18.09.16
 */

public class RegParams extends BaseObservable {

    private enum Field {
        USERNAME(R.string.error_username_empty),
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

    public RegTextWatcher usernameWatcher;
    public RegTextWatcher passwordWatcher;
    public RegTextWatcher emailWatcher;

    public String username = "";
    public String password = "";
    public String email = "";
    private int birthDay;
    private int birthMonth;
    private int birthYear;

    private String mUsernameError = null;
    private String mPasswordError = null;
    private String mEmailError = null;

    RegParams(Context context) {
        mResources = context.getResources();
        usernameWatcher = new RegTextWatcher(Field.USERNAME);
        passwordWatcher = new RegTextWatcher(Field.PASSWORD);
        emailWatcher = new RegTextWatcher(Field.EMAIL);
    }

    private void validate(Field field) {
        switch (field) {
            case USERNAME: {
                setUsernameError(!username.isEmpty()
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
        return mUsernameError == null && mPasswordError == null && mEmailError == null;
    }

    private boolean isEmailCorrect() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    @Bindable
    public String getEmailError() {
        return mEmailError;
    }

    private void setEmailError(String emailError) {
        this.mEmailError = emailError;
        notifyPropertyChanged(BR.emailError);
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
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

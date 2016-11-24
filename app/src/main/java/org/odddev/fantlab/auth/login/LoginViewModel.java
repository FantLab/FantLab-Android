package org.odddev.fantlab.auth.login;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;

import org.odddev.fantlab.BR;
import org.odddev.fantlab.R;

/**
 * @author kenrube
 * @since 17.09.16
 */

public class LoginViewModel extends BaseObservable {

    @IntDef({
            FIELD.USERNAME,
            FIELD.PASSWORD})
    private @interface FIELD {

        int USERNAME = 0;
        int PASSWORD = 1;
    }

    private Resources mResources;

    private String username = "";
    private String password = "";

    private String mUsernameError = null;
    private String mPasswordError = null;

    LoginViewModel(Context context) {
        mResources = context.getResources();
    }

    private void validate(@FIELD int field) {
        switch (field) {
            case FIELD.USERNAME: {
                mUsernameError = username.isEmpty()
                        ? mResources.getString(R.string.error_username_empty)
                        : null;
                notifyPropertyChanged(BR.usernameError);
                break;
            }
            case FIELD.PASSWORD: {
                mPasswordError = password.isEmpty()
                        ? mResources.getString(R.string.error_password_empty)
                        : null;
                notifyPropertyChanged(BR.passwordError);
                break;
            }
        }
    }

    boolean isValid() {
        for (@FIELD int i = 0; i < 2; i++) {
            validate(i);
        }
        return mUsernameError == null && mPasswordError == null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        validate(FIELD.USERNAME);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        validate(FIELD.PASSWORD);
    }

    @Bindable
    public String getUsernameError() {
        return mUsernameError;
    }

    @Bindable
    public String getPasswordError() {
        return mPasswordError;
    }
}

package org.odddev.fantlab.profile;

import android.util.Patterns;

import org.odddev.fantlab.core.validator.Errors;
import org.odddev.fantlab.core.validator.Validator;

import rx.Observable;

/**
 * Developer: Ivan Zolotarev
 * Date: 31.08.16
 */

public class ProfileValidator extends Validator<User> {

    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_PROFILE = 2;

    private int mErrorUserName;
    private int mErrorPassword;
    private int mErrorEmail;
    private int mErrorFullName;
    private int mErrorAltLocation;
    private int mErrorHomepage;
    private int mErrorSkype;
    private int mErrorIcq;
    private int mErrorControlAnswer;

    private int mType;

    public static ProfileValidator newInstance(User user, int type) {
        return new ProfileValidator(user, type);
    }

    protected ProfileValidator(User user, int type) {
        super(user);
        mType = type;
    }

    public Observable<ProfileValidator> getValidatorObservable() {
        return Observable.just(this).map(profileValidator -> {
            validationRun();
            return profileValidator;
        });
    }

    @Override
    protected void validationRun() {
        switch (mType) {
            case TYPE_LOGIN: {
                mErrorUserName = checkString(mData.getUserName());
                mErrorPassword = checkString(mData.getPassword());
                break;
            }
            case TYPE_REGISTER: {
                mErrorUserName = checkString(mData.getUserName());
                mErrorPassword = checkString(mData.getPassword());
                mErrorEmail = checkEmail(mData.getEmail());
                mErrorFullName = checkString(mData.getFullName());
                mErrorAltLocation = checkString(mData.getAltLocation());
                mErrorHomepage = checkUrl(mData.getHomepage());
                mErrorSkype = checkSkype(mData.getSkype());
                mErrorIcq = checkIcq(mData.getIcq());
                mErrorControlAnswer = checkString(mData.getControlAnswer());
                break;
            }
        }
        mFormIsValid = isValid();
    }

    public int getErrorUserName() {
        return mErrorUserName;
    }

    public int getErrorPassword() {
        return mErrorPassword;
    }

    public int getErrorEmail() {
        return mErrorEmail;
    }

    public int getErrorFullName() {
        return mErrorFullName;
    }

    public int getErrorAltLocation() {
        return mErrorAltLocation;
    }

    public int getErrorHomepage() {
        return mErrorHomepage;
    }

    public int getErrorSkype() {
        return mErrorSkype;
    }

    public int getErrorIcq() {
        return mErrorIcq;
    }

    public int getErrorControlAnswer() {
        return mErrorControlAnswer;
    }

    public boolean formIsValid() {
        return mFormIsValid;
    }

    private int checkUrl(String url) {
        int value = checkString(url);
        if (value != NO_ERROR) {
            return value;
        }
        return Patterns.WEB_URL.matcher(url).matches() ?
                NO_ERROR : Errors.ERROR_INCORRECT_URL.textResource;
    }

    private int checkSkype(String skype) {
        // TODO: 01.09.16 modify with regular expression
        return NO_ERROR;
    }


    private int checkIcq(String icq) {
        // TODO: 01.09.16 modify with regular expression
        return NO_ERROR;
    }

    @Override
    protected boolean isValid() {
        switch (mType) {
            case TYPE_LOGIN: {
                return isValidLogin();
            }
            case TYPE_REGISTER: {
                return isValidProfile();
            }
        }
        return true;
    }

    private boolean isValidLogin() {
        return mErrorUserName == NO_ERROR &&
                mErrorPassword == NO_ERROR;
    }

    private boolean isValidProfile() {
        return mErrorUserName == NO_ERROR &&
                mErrorPassword == NO_ERROR &&
                mErrorEmail == NO_ERROR &&
                mErrorFullName == NO_ERROR &&
                mErrorAltLocation == NO_ERROR &&
                mErrorHomepage == NO_ERROR &&
                mErrorSkype == NO_ERROR &&
                mErrorIcq == NO_ERROR &&
                mErrorControlAnswer == NO_ERROR;
    }
}

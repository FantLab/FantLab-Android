package org.odddev.fantlab.profile;

import java.util.Date;

/**
 * @author kenrube
 * @date 31.08.16
 */

public class User {

    enum Gender {
        MALE(0),
        FEMALE(1);

        int mValue;

        Gender(int value) {
            mValue = value;
        }
    }

    private String mUserName;
    private String mPassword;
    private String mEmail;
    private String mFullName;
    private Gender mGender;
    private Date mBirthDay;
    private String mCountry;
    private String mRegion;
    private String mCity;
    private String mAltLocation;
    private String mHomepage;
    private String mSkype;
    private String mIcq;
    private String mControlAnswer;

    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
    }

    public User(String userName, String password, String email, String fullName, Gender gender,
                Date birthDay, String country, String region, String city, String altLocation,
                String homepage, String skype, String icq, String controlAnswer) {
        mUserName = userName;
        mPassword = password;
        mEmail = email;
        mFullName = fullName;
        mGender = gender;
        mBirthDay = birthDay;
        mCountry = country;
        mRegion = region;
        mCity = city;
        mAltLocation = altLocation;
        mHomepage = homepage;
        mSkype = skype;
        mIcq = icq;
        mControlAnswer = controlAnswer;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFullName() {
        return mFullName;
    }

    public Gender getGender() {
        return mGender;
    }

    public Date getBirthDay() {
        return mBirthDay;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getRegion() {
        return mRegion;
    }

    public String getCity() {
        return mCity;
    }

    public String getAltLocation() {
        return mAltLocation;
    }

    public String getHomepage() {
        return mHomepage;
    }

    public String getSkype() {
        return mSkype;
    }

    public String getIcq() {
        return mIcq;
    }

    public String getControlAnswer() {
        return mControlAnswer;
    }
}

package org.odddev.fantlab.profile;

import org.odddev.fantlab.core.validator.Validator;

/**
 * Developer: Ivan Zolotarev
 * Date: 31.08.16
 */

public class ProfileValidator extends Validator<User> {

    public ProfileValidator(User data) {
        super(data);
    }

    @Override
    protected void validationRun() {

    }

    @Override
    protected boolean isValid() {
        return false;
    }
}

package org.odddev.fantlab.core.validator;

/**
 * Developer: Ivan Zolotarev
 * Date: 06.09.16
 */

public class ValidatorException extends Throwable {

    @Override
    public String getMessage() {
        return "Form is not valid";
    }
}

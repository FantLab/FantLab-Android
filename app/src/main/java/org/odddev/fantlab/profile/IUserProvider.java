package org.odddev.fantlab.profile;

import rx.Observable;

/**
 * Developer: Ivan Zolotarev
 * Date: 15.09.16
 */

public interface IUserProvider {

    Observable<Void> login(User user);
}

package org.odddev.fantlab.auth;

import rx.Observable;

/**
 * @author kenrube
 * @date 15.09.16
 */

public interface IAuthProvider {

    Observable<Void> login(String login, String password);
}

package org.odddev.fantlab.auth;

import rx.Observable;

/**
 * @author kenrube
 * @date 15.09.16
 */

public interface IAuthProvider {

    Observable<Boolean> login(String login, String password);

    Observable<Boolean> register(String login, String password, String email);
}

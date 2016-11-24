package org.odddev.fantlab.auth;

import rx.Observable;

/**
 * @author kenrube
 * @since 15.09.16
 */

public interface IAuthProvider {

    Observable<Boolean> login(String username, String password);

    Observable<Boolean> register(String username, String password, String email);
}

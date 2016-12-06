package org.odddev.fantlab.auth;

import rx.Single;

/**
 * @author kenrube
 * @since 15.09.16
 */

public interface IAuthProvider {

    Single<Boolean> login(String username, String password);

    Single<Boolean> register(String username, String password, String email);
}

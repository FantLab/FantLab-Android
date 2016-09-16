package org.odddev.fantlab.profile;

import rx.Observable;

/**
 * @author kenrube
 * @date 15.09.16
 */

public interface IUserProvider {

    Observable<Void> login(/*User user*/String login, String password);
}

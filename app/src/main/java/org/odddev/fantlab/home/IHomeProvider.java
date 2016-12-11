package org.odddev.fantlab.home;

import rx.Single;

/**
 * @author kenrube
 * @since 11.12.16
 */

public interface IHomeProvider {

    Single<String> getUserName();

    void clearCookie();
}

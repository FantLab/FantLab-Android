package org.odddev.fantlab.auth;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.login.LoginFragment;
import org.odddev.fantlab.core.utils.FragmentUtils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author kenrube
 * @date 23.08.16
 */

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        FragmentUtils.initFragment(this, R.id.container, new LoginFragment(), false);
    }
}

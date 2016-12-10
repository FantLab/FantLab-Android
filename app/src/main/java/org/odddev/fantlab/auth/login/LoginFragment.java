package org.odddev.fantlab.auth.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthActivity;
import org.odddev.fantlab.auth.AuthRouter;
import org.odddev.fantlab.databinding.LoginFragmentBinding;

/**
 * @author kenrube
 * @since 30.08.16
 */

public class LoginFragment extends MvpAppCompatFragment implements ILoginView, ILoginActions {

    @InjectPresenter
    LoginPresenter presenter;

    private LoginFragmentBinding binding;
    private AuthRouter router;

    private LoginValidator loginValidator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router = new AuthRouter((AuthActivity) getActivity(), R.id.container);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.setHandler(this);
        loginValidator = new LoginValidator(getContext());
        binding.setLoginValidator(loginValidator);
        binding.setForgotPass(false);
    }

    @Override
    public void login() {
        presenter.login(loginValidator);
    }

    @Override
    public void register() {
        router.routeToReg();
    }

    @Override
    public void forgotPass() {
        // todo 3.1, 3.2, 3.3
    }

    @Override
    public void entry() {
        router.routeToHome(false);
    }

    @Override
    public void showFieldsInvalid() {
        binding.setForgotPass(false);
    }

    @Override
    public void showLoginResult(boolean loggedIn) {
        if (loggedIn) {
            router.routeToHome(true);
        } else {
            binding.setForgotPass(true);
            showError(getString(R.string.auth_login_error));
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }
    // todo 1.10
}

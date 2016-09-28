package org.odddev.fantlab.auth.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.R;
import org.odddev.fantlab.auth.AuthRouter;
import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.databinding.LoginFragmentBinding;

/**
 * @author kenrube
 * @date 30.08.16
 */
public class LoginFragment extends Fragment implements ILoginView {

    private static final int PRESENTER_ID = LoginPresenter.class.getSimpleName().hashCode();

    private LoginPresenter mPresenter;
    private LoginFragmentBinding mBinding;
    private AuthRouter mRouter;

    private LoginParams mLoginParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(PRESENTER_ID, LoginPresenter::new);
        mRouter = new AuthRouter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LoginFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBinding.setActionsHandler(this);
        mLoginParams = new LoginParams(getContext());
        mBinding.setLoginParams(mLoginParams);
        mBinding.setForgotPassVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    @Override
    public void onStop() {
        mPresenter.detachView(this);
        super.onStop();
    }

    public void login() {
        if (mLoginParams.isValid()) {
            mPresenter.login(mLoginParams.login, mLoginParams.password);
            mBinding.setForgotPassVisible(false);
        }
    }

    public void register() {
        mRouter.routeToReg();
    }

    public void forgotPass() {
        //TODO open Dialog for pass change
    }

    public void entry() {
        mRouter.routeToHome(false);
    }

    @Override
    public void showLoginResult(boolean loggedIn) {
        if (loggedIn) {
            mRouter.routeToHome(true);
        } else {
            mBinding.setForgotPassVisible(true);
            showError(getString(R.string.error_login));
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }
    // TODO refactor layout (styles etc)
}

package org.odddev.fantlab.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.databinding.LoginFragmentBinding;

import timber.log.Timber;

/**
 * @author kenrube
 * @date 30.08.16
 */
// TODO: 23.08.16 refactor layout (styles etc)
public class LoginFragment extends Fragment implements ILoginView {

    private LoginPresenter mPresenter;
    private LoginFragmentBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(this.hashCode(), LoginPresenter::new);
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

    public void login(View view) {
        mPresenter.login(mBinding.username.getText().toString(),
                mBinding.password.getText().toString());
    }

    public void register(View view) {
        Timber.d("Register");
    }

    public void entry(View view) {
        Timber.d("Entry");
    }

    @Override
    public void showLoggedIn() {
        Timber.d("LoggedIn");
    }
}

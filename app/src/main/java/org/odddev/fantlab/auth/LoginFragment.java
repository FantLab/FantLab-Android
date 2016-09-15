package org.odddev.fantlab.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.odddev.fantlab.R;
import org.odddev.fantlab.core.layers.presenter.PresenterManager;
import org.odddev.fantlab.core.view.BaseBindingFragment;
import org.odddev.fantlab.databinding.LoginFragmentBinding;
import org.odddev.fantlab.profile.ProfileValidator;
import org.odddev.fantlab.profile.User;

import timber.log.Timber;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */
// TODO: 23.08.16 refactor layout (styles etc)
public class LoginFragment extends BaseBindingFragment<LoginFragmentBinding>
        implements ILoginView {

    private LoginPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = PresenterManager.getPresenter(this.hashCode(), LoginPresenter::new);
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
        mPresenter.checkForm(new User(mBinding.username.getText().toString(),
                mBinding.password.getText().toString()));
    }

    public void register(View view) {
        Timber.d("Register");
    }

    public void entry(View view) {
        Timber.d("Entry");
    }

    @Override
    public void showValidator(ProfileValidator validator) {

    }

    @Override
    public void showError(int errorResource) {

    }

    @Override
    public void success() {

    }

    @Override
    public void showData(User data) {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showLoading() {

    }
}

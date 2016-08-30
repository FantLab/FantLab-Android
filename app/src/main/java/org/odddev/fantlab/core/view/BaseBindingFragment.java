package org.odddev.fantlab.core.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Developer: Ivan Zolotarev
 * Date: 30.08.16
 */

public abstract class BaseBindingFragment<B extends ViewDataBinding> extends BaseFragment {

    protected B mBinding;

    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if (mBinding != null) {
            mBinding.unbind();
        }
        super.onDestroyView();
    }
}

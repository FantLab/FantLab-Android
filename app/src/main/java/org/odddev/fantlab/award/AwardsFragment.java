package org.odddev.fantlab.award;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.AwardsFragmentBinding;
import org.odddev.fantlab.home.HomeOptionSelectListener;

/**
 * @author kenrube
 * @since 10.12.16
 */

public class AwardsFragment extends MvpAppCompatFragment {

    private AwardsFragmentBinding binding;
    private HomeOptionSelectListener listener;

    public AwardsFragment() {
    }

    public AwardsFragment(HomeOptionSelectListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AwardsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initToolbar();
        setHasOptionsMenu(true);
    }

    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.nav_awards);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                listener.onHomeOptionSelected();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

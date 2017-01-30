package org.odddev.fantlab.award;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.AwardsFragmentBinding;

import java.util.List;

/**
 * @author kenrube
 * @since 10.12.16
 */

public class AwardsFragment extends MvpAppCompatFragment implements IAwardsView {

	private AwardsFragmentBinding binding;
	private AwardsAdapter adapter;

	@InjectPresenter
	AwardsPresenter presenter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		binding = AwardsFragmentBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		presenter.getAwards();

		initToolbar();
		setHasOptionsMenu(true);

		initRecyclerView();
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

	private void initRecyclerView() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		binding.awards.setLayoutManager(layoutManager);
		adapter = new AwardsAdapter();
		binding.awards.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				((DrawerLayout) getActivity().findViewById(R.id.drawer_layout))
						.openDrawer(GravityCompat.START);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void showAwards(List<Award> awards) {
		adapter.setAwards(awards);
	}

	@Override
	public void showError(String message) {
		Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
	}
}

package org.odddev.fantlab.award;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.odddev.fantlab.R;
import org.odddev.fantlab.databinding.AwardItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kenrube
 * @since 07.12.16
 */

public class AwardsAdapter extends RecyclerView.Adapter<AwardsAdapter.AwardViewHolder> {

    private List<Award> awards;

    public AwardsAdapter() {
        awards = new ArrayList<>();
    }

    @Override
    public AwardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AwardViewHolder(DataBindingUtil.inflate(inflater, R.layout.award_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AwardViewHolder holder, int position) {
        holder.binding.setAward(awards.get(position));
    }

    @Override
    public int getItemCount() {
        return awards.size();
    }

    void setAwards(List<Award> awards) {
        this.awards = awards;
        notifyDataSetChanged();
    }

    class AwardViewHolder extends RecyclerView.ViewHolder {

        AwardItemBinding binding;

        AwardViewHolder(AwardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

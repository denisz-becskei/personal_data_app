package com.deniszbecskei.personaldata;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> implements Filterable {
    private final Context mContext;
    private ArrayList<SearchItem> mSearchItemData = new ArrayList<>();
    private ArrayList<SearchItem> mSearchItemDataAll = new ArrayList<>();
    private final NotificationHandler mNotiHandler;
    private final String caller;
    private int lastPosition = -1;

    public SearchItemAdapter(Context mContext, ArrayList<SearchItem> itemsData, String caller) {
        this.mContext = mContext;
        this.mSearchItemDataAll = itemsData;
        this.mSearchItemData = itemsData;
        this.caller = caller;
        this.mNotiHandler = new NotificationHandler(mContext);
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SearchItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = mSearchItemDataAll.size();
                results.values = mSearchItemDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SearchItem si : mSearchItemDataAll) {
                    if (si.getPerson().getName().getText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(si);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSearchItemData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public SearchItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.person_listing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemAdapter.ViewHolder holder, int position) {
        SearchItem currentItem = mSearchItemData.get(position);
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation;
            if (caller.equals(SearchPeopleActivity.class.getName())) {
                animation = AnimationUtils.loadAnimation(mContext, R.anim.float_in_from_left);
            } else {
                animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_right);
            }
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mSearchItemData.size();
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPersonNameText;
        private final TextView mPersonInfoText;
        private final ImageView mPersonImg;

        ViewHolder(View itemView) {
            super(itemView);

            mPersonNameText = itemView.findViewById(R.id.person_name);
            mPersonInfoText = itemView.findViewById(R.id.person_info);
            mPersonImg = itemView.findViewById(R.id.person_img);
            itemView.findViewById(R.id.person_details).setOnClickListener(v -> {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("STUFF_I_NEED", mPersonNameText.getText().toString());
                mNotiHandler.send("You requested the data of " + mPersonNameText.getText().toString().substring(0, mPersonNameText.getText().toString().length() - 3));
                mContext.startActivity(intent);
            });
        }

        void bindTo(SearchItem currentItem) {
            String name = currentItem.getPerson().getName().getText() + "(" + currentItem
                    .getPerson().getGender() + ")";
            mPersonNameText.setText(name);
            mPersonInfoText.setText(currentItem.getPerson().getBirthDateString());
            Glide.with(mContext).load(currentItem.getPerson().getPhoto()).into(mPersonImg);
        }
    }
}



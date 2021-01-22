package com.wuzx.atest.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzx.atest.R;

public class BrowseMediaItemsAdapter extends  RecyclerView.Adapter<BrowseMediaItemsAdapter.ViewHolder>{


    public void init() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected void subscribe(){

    }

    protected void unsubscribe(){

    }

    public void setRoot(String root) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView description;
        private final ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.item_description);
            icon = itemView.findViewById(R.id.item_icon);
        }
    }
}

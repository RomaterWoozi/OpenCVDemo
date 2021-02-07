package com.wuzx.atest.opencv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzx.atest.R;

import java.util.List;

/**
 * @author WuZX
 * 时间  2021/2/7 11:41
 * RecyclerView.Adapter<TestViewAdapter.TestViewHolder>
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder> {

    private List<ItemDto> dataModel;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnSelectorListener mSelectorListener;

    public RecyclerListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_layout, parent, false));
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,final int position) {
        ItemDto itemDto=dataModel.get(position);
        holder.mTextView.setText(dataModel.get(position).getDesc());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectorListener.onSelect(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview);
        }
    }


    public interface OnSelectorListener {
        void onSelect(View view, int position);
    }

    public void setSelectorListener(OnSelectorListener mSelectorListener) {
        this.mSelectorListener = mSelectorListener;
    }

    public List<ItemDto> getDataModel() {
        return dataModel;
    }

}

package com.wuzx.atest.media;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzx.atest.R;
import com.wuzx.atest.databinding.MediaFfmpegLayoutBinding;

public class FFmpegListActivity extends AppCompatActivity {
     MediaFfmpegLayoutBinding mflb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mflb =DataBindingUtil.setContentView(this, R.layout.media_ffmpeg_layout);

        initView();
    }

    private void initView() {
         mflb.listView.setAdapter(new RecyclerView.Adapter() {
             @NonNull
             @Override
             public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                 return null;
             }

             @Override
             public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

             }

             @Override
             public int getItemCount() {
                 return 0;
             }
         });

    }





}

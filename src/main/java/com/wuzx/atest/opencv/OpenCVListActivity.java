package com.wuzx.atest.opencv;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wuzx.atest.R;
import com.wuzx.atest.databinding.OpencvLayoutBinding;
import com.wuzx.atest.opencv.adapter.RecyclerListAdapter;
import com.wuzx.atest.opencv.adapter.RecyclerListAdapter.OnSelectorListener;
import com.wuzx.atest.opencv.utils.ChapterUtils;

public class OpenCVListActivity extends AppCompatActivity {
    OpencvLayoutBinding opencvLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opencvLayout = DataBindingUtil.setContentView(this, R.layout.opencv_layout);

    }

    public void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        opencvLayout.recyclerview.setLayoutManager(linearLayoutManager);
        DividerItemDecoration vertical = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        vertical.setDrawable(getDrawable(R.drawable.pwd_divider));
        opencvLayout.recyclerview.addItemDecoration(vertical);
        RecyclerListAdapter recyclerListAdapter=new RecyclerListAdapter(this);
        recyclerListAdapter.getDataModel().addAll(ChapterUtils.getChapters());
        opencvLayout.recyclerview.setAdapter(recyclerListAdapter);

    }


    class ItemSelectionListener implements OnSelectorListener {
        @Override
        public void onSelect(View view, int position) {

        }
    }

}

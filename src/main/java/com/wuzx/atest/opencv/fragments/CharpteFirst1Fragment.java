package com.wuzx.atest.opencv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wuzx.atest.R;

public class CharpteFirst1Fragment extends Fragment implements View.OnClickListener {


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button processBtn = new Button(getContext());
        processBtn.setId(R.id.process_btn);
        Button takePicBtn = new Button(getContext());
        takePicBtn.setId(R.id.take_pic_btn);
        Button selectPicBtn = new Button(getContext());
        selectPicBtn.setId(R.id.select_pic_btn);

        linearLayout.addView(processBtn, 0);
        linearLayout.addView(takePicBtn, 1);
        linearLayout.addView(selectPicBtn, 2);

        return linearLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.process_btn:
                break;
            case R.id.take_pic_btn:
                break;
            case R.id.select_pic_btn:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

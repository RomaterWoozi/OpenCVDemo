package com.wuzx.atest.base;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.wuzx.atest.R;
import com.wuzx.atest.adapters.TestListAdapter;
import com.wuzx.atest.adapters.TestListAdapter.TestListItem;

public abstract class AbstractTestListActivity extends ListActivity {


    protected TestListAdapter mAdapter;

    protected void setTestListAdapter(TestListAdapter adapter) {
        mAdapter = adapter;
        adapter.setData();
        setListAdapter(mAdapter);
        mAdapter.loadTestResults();
    }

    private Intent getIntent(int position) {
        TestListItem item = mAdapter.getItem(position);
        return item.intent;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_TYPE_MASK)
                == Configuration.UI_MODE_TYPE_TELEVISION) {
            getWindow().requestFeature(Window.FEATURE_OPTIONS_PANEL);
        }
        setContentView(R.layout.list_content);
    }



    /** Launch the activity when its {@link ListView} item is clicked. */
    @Override
    protected final void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        handleItemClick(listView, view, position, id);
    }

    /** Override this in subclasses instead of onListItemClick */
    protected void handleItemClick(ListView listView, View view, int position, long id) {
        Intent intent = getIntent(position);
        if(intent!=null){
            startActivity(intent);
        }
    }
}

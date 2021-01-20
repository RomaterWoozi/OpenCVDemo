package com.wuzx.atest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wuzx.atest.R;

import java.util.ArrayList;
import java.util.List;

public abstract class TestListAdapter extends BaseAdapter {
    /** Activities implementing {@link Intent#ACTION_MAIN} and this will appear in the list. */
    public static final String CATEGORY_MANUAL_TEST = "android.wuzx.intent.category.MANUAL_TEST";

    /** View type for a category of tests like "Sensors" or "Features" */
    private static final int CATEGORY_HEADER_VIEW_TYPE = 0;

    /** View type for an actual test like the Accelerometer test. */
    private static final int TEST_VIEW_TYPE = 1;

    /** Padding around the text views and icons. */
    private static final int PADDING = 10;
    private List<TestListItem> mRows = new ArrayList<TestListItem>();


    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public TestListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void loadTestResults() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mRows.size();
    }

    @Override
    public TestListItem getItem(int position) {
        return mRows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            int layout = getLayout(position);
            textView = (TextView) mLayoutInflater.inflate(layout, parent, false);
        } else {
            textView = (TextView) convertView;
        }

        TestListItem item = getItem(position);
        textView.setText(item.title);
        textView.setPadding(PADDING, 0, PADDING, 0);
        textView.setCompoundDrawablePadding(PADDING);
        if(item.intent==null){
            textView.setEnabled(false);
            textView.setClickable(false);
        }
        return textView;
    }

    private int getLayout(int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case CATEGORY_HEADER_VIEW_TYPE:
                return R.layout.test_category_row;
            case TEST_VIEW_TYPE:
                return android.R.layout.simple_list_item_1;
            default:
                throw new IllegalArgumentException("Illegal view type: " + viewType);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isTest() ? TEST_VIEW_TYPE : CATEGORY_HEADER_VIEW_TYPE;
    }

    public void setData(){
        mRows=getRows();
    }

    public abstract List<TestListItem> getRows();


    /** {@link ListView} row that is either a test category header or a test. */
    public static class TestListItem {

        /** Title shown in the {@link ListView}. */
        final String title;

        /** Test name with class and test ID to uniquely identify the test. Null for categories. */
        final String testName;

        /** Intent used to launch the activity from the list. Null for categories. */
        public final Intent intent;

        /** Features necessary to run this test. */
        final String[] requiredFeatures;

        /** Configs necessary to run this test. */
        final String[] requiredConfigs;

        /** Features such that, if any present, the test gets excluded from being shown. */
        final String[] excludedFeatures;

        /** If any of of the features are present the test is meaningful to run. */
        final String[] applicableFeatures;

        public static TestListItem newTest(Context context, int titleResId, String testName,
                                           Intent intent, String[] requiredFeatures, String[] excludedFeatures,
                                           String[] applicableFeatures) {
            return newTest(context.getString(titleResId), testName, intent, requiredFeatures,
                    excludedFeatures, applicableFeatures);
        }

        public static TestListItem newTest(Context context, int titleResId, String testName,
                                           Intent intent, String[] requiredFeatures, String[] excludedFeatures) {
            return newTest(context.getString(titleResId), testName, intent, requiredFeatures,
                    excludedFeatures, null);
        }

        public static TestListItem newTest(Context context, int titleResId, String testName,
                                           Intent intent, String[] requiredFeatures) {
            return newTest(context.getString(titleResId), testName, intent, requiredFeatures, null,
                    null);
        }

        public static TestListItem newTest(String title, String testName, Intent intent,
                                           String[] requiredFeatures, String[] requiredConfigs, String[] excludedFeatures,
                                           String[] applicableFeatures) {
            return new TestListItem(title, testName, intent, requiredFeatures, requiredConfigs,
                    excludedFeatures, applicableFeatures);
        }

        public static TestListItem newTest(String title, String testName, Intent intent,
                                           String[] requiredFeatures, String[] excludedFeatures, String[] applicableFeatures) {
            return new TestListItem(title, testName, intent, requiredFeatures, null, excludedFeatures,
                    applicableFeatures);
        }

        public static TestListItem newTest(String title, String testName, Intent intent,
                                           String[] requiredFeatures, String[] excludedFeatures) {
            return new TestListItem(title, testName, intent, requiredFeatures, null, excludedFeatures,
                    null);
        }

        public static TestListItem newTest(String title, String testName, Intent intent,
                                           String[] requiredFeatures) {
            return new TestListItem(title, testName, intent, requiredFeatures, null, null, null);
        }

        public static TestListItem newCategory(Context context, int titleResId) {
            return newCategory(context.getString(titleResId));
        }

        public static TestListItem newCategory(String title) {
            return new TestListItem(title, null, null, null, null, null, null);
        }

        protected TestListItem(String title, String testName, Intent intent,
                               String[] requiredFeatures, String[] excludedFeatures, String[] applicableFeatures) {
            this(title, testName, intent, requiredFeatures, null, excludedFeatures, applicableFeatures);
        }

        protected TestListItem(String title, String testName, Intent intent,
                               String[] requiredFeatures, String[] requiredConfigs, String[] excludedFeatures,
                               String[] applicableFeatures) {
            this.title = title;
            this.testName = testName;
            this.intent = intent;
            this.requiredFeatures = requiredFeatures;
            this.requiredConfigs = requiredConfigs;
            this.excludedFeatures = excludedFeatures;
            this.applicableFeatures = applicableFeatures;
        }

        boolean isTest() {
            return intent != null;
        }
    }




}

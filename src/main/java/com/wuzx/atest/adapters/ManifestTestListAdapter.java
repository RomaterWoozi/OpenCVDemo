package com.wuzx.atest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.wuzx.atest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ManifestTestListAdapter extends TestListAdapter{
    private static final String TEST_CATEGORY_META_DATA = "test_category";

    private static final String TEST_PARENT_META_DATA = "test_parent";

    private static final String TEST_REQUIRED_FEATURES_META_DATA = "test_required_features";

    private static final String TEST_EXCLUDED_FEATURES_META_DATA = "test_excluded_features";

    private static final String TEST_APPLICABLE_FEATURES_META_DATA = "test_applicable_features";

    private static final String TEST_REQUIRED_CONFIG_META_DATA = "test_required_configs";

    private static final String CONFIG_VOICE_CAPABLE = "config_voice_capable";

    private final HashSet<String> mDisabledTests;

    private Context mContext;

    private String mTestParent;

    public ManifestTestListAdapter(Context context, String testParent, String[] disabledTestArray) {
        super(context);
        mContext = context;
        mTestParent = testParent;
        mDisabledTests = new HashSet<>(disabledTestArray.length);
        for (int i = 0; i < disabledTestArray.length; i++) {
            mDisabledTests.add(disabledTestArray[i]);
        }
    }

    public ManifestTestListAdapter(Context context, String testParent) {
        this(context, testParent, context.getResources().getStringArray(R.array.disabled_tests));
    }




    @SuppressLint("WrongConstant")
    List<ResolveInfo> getResolveInfoForParent() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(CATEGORY_MANUAL_TEST);
        mainIntent.setPackage(mContext.getPackageName());

        PackageManager packageManager = mContext.getPackageManager();
     List<ResolveInfo> list = packageManager.queryIntentActivities(mainIntent,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
        int size = list.size();

        List<ResolveInfo> matchingList = new ArrayList<ResolveInfo>();
        for (int i = 0; i < size; i++) {
            ResolveInfo info = list.get(i);
            String parent = getTestParent(info.activityInfo.metaData);
            if ((mTestParent == null && parent == null)
                    || (mTestParent != null && mTestParent.equals(parent))) {
                matchingList.add(info);
            }
        }
        return matchingList;
    }


    @Override
    public List<TestListItem> getRows() {
        /*
         * 1. Get all the tests belonging to the test parent.
         * 2. Get all the tests keyed by their category.
         * 3. Flatten the tests and categories into one giant list for the list view.
         */

        List<ResolveInfo> info = getResolveInfoForParent();
        Map<String, List<TestListItem>> testsByCategory = getTestsByCategory(info);

        List<String> testCategories = new ArrayList<String>(testsByCategory.keySet());
        Collections.sort(testCategories);

        List<TestListItem> allRows = new ArrayList<TestListItem>();
        for (String testCategory : testCategories) {
            List<TestListItem> tests = filterTests(testsByCategory.get(testCategory));
            if (!tests.isEmpty()) {
                allRows.add(TestListItem.newCategory(testCategory));
                Collections.sort(tests, new Comparator<TestListItem>() {
                    @Override
                    public int compare(TestListItem item, TestListItem otherItem) {
                        return item.title.compareTo(otherItem.title);
                    }
                });
                allRows.addAll(tests);
            }
        }
        return allRows;
    }

    private List<TestListItem> filterTests(List<TestListItem> testListItems) {
        List<TestListItem> filterTests=new ArrayList<>();
        for (TestListItem testItem :
                testListItems) {
            if(!hasAnyFeature(testItem.excludedFeatures)&& hasAllFeatures(testItem.requiredFeatures)){
                if (testItem.applicableFeatures == null || hasAnyFeature(testItem.applicableFeatures)) {
                    filterTests.add(testItem);
                }
            }
        }
        return filterTests;
    }

    private boolean hasAllFeatures(String[] features) {
        if (features != null) {
            PackageManager packageManager = mContext.getPackageManager();
            for (String feature : features) {
                if (!packageManager.hasSystemFeature(feature)) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean hasAnyFeature(String[] features) {
        if (features != null) {
            PackageManager packageManager = mContext.getPackageManager();
            for (String feature : features) {
                if (packageManager.hasSystemFeature(feature)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * @author WuZX
    * 时间  2021/1/19 15:50
    *  测试
    */
    Map<String, List<TestListItem>> getTestsByCategory(List<ResolveInfo> list) {
        Map<String, List<TestListItem>> testsByCategory =
                new HashMap<String, List<TestListItem>>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo == null || mDisabledTests.contains(info.activityInfo.name)) {
                Log.w("WuZX", "ignoring disabled test: " + info.activityInfo.name);
                continue;
            }
            String title = getTitle(mContext, info.activityInfo);
            String testName = info.activityInfo.name;
            Intent intent = getActivityIntent(info.activityInfo);
            String[] requiredFeatures = getRequiredFeatures(info.activityInfo.metaData);
            String[] requiredConfigs = getRequiredConfigs(info.activityInfo.metaData);
            String[] excludedFeatures = getExcludedFeatures(info.activityInfo.metaData);
            String[] applicableFeatures = getApplicableFeatures(info.activityInfo.metaData);
            TestListItem item = TestListItem.newTest(title, testName, intent, requiredFeatures,
                    requiredConfigs, excludedFeatures, applicableFeatures);

            String testCategory = getTestCategory(mContext, info.activityInfo.metaData);
            addTestToCategory(testsByCategory, testCategory, item);
        }

        return testsByCategory;
    }

    private String[] getApplicableFeatures(Bundle metaData) {
        if (metaData == null) {
            return null;
        } else {
            String value = metaData.getString(TEST_APPLICABLE_FEATURES_META_DATA);
            if (value == null) {
                return null;
            } else {
                return value.split(":");
            }
        }
    }

    private String[] getExcludedFeatures(Bundle metaData) {
        if (metaData == null) {
            return null;
        } else {
            String value = metaData.getString(TEST_EXCLUDED_FEATURES_META_DATA);
            if (value == null) {
                return null;
            } else {
                return value.split(":");
            }
        }
    }

    static String[] getRequiredFeatures(Bundle metaData) {
        if (metaData == null) {
            return null;
        } else {
            String value = metaData.getString(TEST_REQUIRED_FEATURES_META_DATA);
            if (value == null) {
                return null;
            } else {
                return value.split(":");
            }
        }
    }

    static String getTestCategory(Context context, Bundle metaData) {
        String testCategory = null;
        if (metaData != null) {
            testCategory = metaData.getString(TEST_CATEGORY_META_DATA);
        }
        if (testCategory != null) {
            return testCategory;
        } else {
            return context.getString(R.string.test_category_other);
        }
    }

    static String[] getRequiredConfigs(Bundle metaData) {
        if (metaData == null) {
            return null;
        } else {
            String value = metaData.getString(TEST_REQUIRED_CONFIG_META_DATA);
            if (value == null) {
                return null;
            } else {
                return value.split(":");
            }
        }
    }


    static void addTestToCategory(Map<String, List<TestListItem>> testsByCategory,
                                  String testCategory, TestListItem item) {
        List<TestListItem> tests;
        if (testsByCategory.containsKey(testCategory)) {
            tests = testsByCategory.get(testCategory);
        } else {
            tests = new ArrayList<TestListItem>();
        }
        testsByCategory.put(testCategory, tests);
        tests.add(item);
    }


    private Intent getActivityIntent(ActivityInfo activityInfo) {
        Intent intent = new Intent();
        intent.setClassName(activityInfo.packageName, activityInfo.name);
        return intent;
    }

    static String getTitle(Context context, ActivityInfo activityInfo) {
        if (activityInfo.labelRes != 0) {
            return context.getString(activityInfo.labelRes);
        } else {
            return activityInfo.name;
        }
    }

    static String getTestParent(Bundle metaData) {
        return metaData != null ? metaData.getString(TEST_PARENT_META_DATA) : null;
    }


}


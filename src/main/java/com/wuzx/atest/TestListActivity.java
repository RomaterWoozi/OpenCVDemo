package com.wuzx.atest;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.wuzx.atest.adapters.ManifestTestListAdapter;
import com.wuzx.atest.base.AbstractTestListActivity;
import com.wuzx.atest.utils.Version;



public class TestListActivity extends AbstractTestListActivity implements View.OnClickListener{
    private static final int TEST_PERMISSION_REQUEST = 1;

    private String TAG="WuZX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(
                    getApplicationInfo().packageName, PackageManager.GET_PERMISSIONS);

            if (packageInfo.requestedPermissions != null) {
                for (String permission : packageInfo.requestedPermissions) {
                    Log.v(TAG, "Checking permissions for: " + permission);
                    try {
                        PermissionInfo info = pm.getPermissionInfo(permission, 0);
                        if ((info.protectionLevel & PermissionInfo.PROTECTION_DANGEROUS) == 0) {
                            continue;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.v(TAG, "Checking permissions for: " + permission + "not found");
                        continue;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(packageInfo.requestedPermissions,
                                    TEST_PERMISSION_REQUEST);
                            return;
                        }
                    }
                }
            }
            createContinue();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to load package's permissions", e);
            Toast.makeText(this, R.string.runtime_permissions_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == TEST_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createContinue();
                return;
            }
            Log.v(TAG, "Permission not granted.");
            Toast.makeText(this, R.string.runtime_permissions_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void createContinue() {
        setTitle(getString(R.string.title_version, Version.getVersionName(this)));

        if (!getWindow().hasFeature(Window.FEATURE_ACTION_BAR)) {
            View footer = getLayoutInflater().inflate(R.layout.test_list_footer, null);

            footer.findViewById(R.id.clear).setOnClickListener(this);
            footer.findViewById(R.id.view).setOnClickListener(this);
            footer.findViewById(R.id.export).setOnClickListener(this);
//            getListView().addFooterView(footer);
        }

        setTestListAdapter(new ManifestTestListAdapter(this, null));
    }

    @Override
    public void onClick(View view) {

    }
}

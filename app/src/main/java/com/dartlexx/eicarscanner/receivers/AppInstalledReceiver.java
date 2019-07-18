package com.dartlexx.eicarscanner.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.dartlexx.eicarscanner.App;
import com.dartlexx.eicarscanner.service.ScanService;

public class AppInstalledReceiver extends BroadcastReceiver {

    private static final String TAG = AppInstalledReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final Uri data = intent.getData();

        if (Intent.ACTION_PACKAGE_REMOVED.equals(action) && data != null) {
            String packageName = data.getEncodedSchemeSpecificPart();
            Log.d(TAG, "Detected app was removed: " + packageName);

            ((App) context.getApplicationContext())
                    .getAppComponent()
                    .getAvDispatcher()
                    .checkRemovedApp(packageName);
        } else if (Intent.ACTION_PACKAGE_ADDED.equals(action) ||
                Intent.ACTION_PACKAGE_CHANGED.equals(action)) {

            if (data == null) {
                ScanService.startFullScan(context);
                return;
            }
            String packageName = data.getEncodedSchemeSpecificPart();
            Log.d(TAG, "Detected app installed or updated: " + packageName);

            final PackageManager packManager = context.getPackageManager();
            try {
                ApplicationInfo info = packManager.getApplicationInfo(packageName, 0);
                ScanService.startSingleAppScan(context, info);
            } catch (PackageManager.NameNotFoundException exc) {
                ScanService.startFullScan(context);
            }
        }
    }
}

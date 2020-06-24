package com.resfandiari.cafebazaar_market;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.farsitel.bazaar.IUpdateCheckService;

import java.util.List;

//address class:
//https://github.com/easazade/cafebazaar/blob/master/android/src/main/java/ir/easazade/cafebazaar/BazzarUpdateChecker.java

public class UpdateServiceConnection implements ServiceConnection {
    private IUpdateCheckService service;
    private UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";
    private Activity activity;

    public UpdateServiceConnection(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("No activity available!");
        }

        this.activity = activity;

        Log.i(TAG, "initService()");
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        Intent explicitIntent = convertImplicitIntentToExplicitIntent(activity.getPackageManager(), i);
        activity.bindService(explicitIntent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = IUpdateCheckService.Stub
                .asInterface((IBinder) iBinder);
        Log.d(TAG, "onServiceConnected(): Connected");
    }

    void checkForNewUpdate(final Runnable onUpdateAvailable, final Runnable onNoUpdateAvailable) {

        if (service != null) {
            doCheck(onUpdateAvailable, onNoUpdateAvailable);
        } else {
            new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doCheck(onUpdateAvailable, onNoUpdateAvailable);
                }
            }, 1000);
        }
    }

    private void doCheck(Runnable onUpdateAvailable, Runnable onNoUpdateAvailable) {
        try {
            String appPackageName = activity.getApplication().getPackageName();
            long versionCode = service.getVersionCode(appPackageName);
            if (versionCode != -1L) {
                onUpdateAvailable.run();
            } else {
                onNoUpdateAvailable.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
        Log.d(TAG, "onServiceDisconnected(): Disconnected");
    }


    /**
     * This is our function to un-binds this activity from our service.
     */
    public void releaseService() {
        activity.unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    private Intent convertImplicitIntentToExplicitIntent(PackageManager pm, Intent implicitIntent) {
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}

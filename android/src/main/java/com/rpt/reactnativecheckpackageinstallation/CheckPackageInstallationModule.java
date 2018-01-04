package com.rpt.reactnativecheckpackageinstallation;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;

public class CheckPackageInstallationModule extends ReactContextBaseJavaModule {
    Context ctx;
    public CheckPackageInstallationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.ctx = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "CheckPackageInstallation";
    }

    @ReactMethod
    public void isPackageInstalled(String packageName, Callback cb) {
        PackageManager pm = this.ctx.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            cb.invoke(true);
        } catch (Exception e) {
            cb.invoke(false);
        }
    }


    @ReactMethod
    public void openApp(String packageName) {
        Intent LaunchIntent = this.ctx.getPackageManager().getLaunchIntentForPackage(packageName);
        this.ctx.startActivity(LaunchIntent);
    }

    @ReactMethod
    public void openAppInStore(String packageName) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            this.ctx.startActivity(i);
        } catch (Exception anfe) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            this.ctx.startActivity(i);
        }
    }


    
}

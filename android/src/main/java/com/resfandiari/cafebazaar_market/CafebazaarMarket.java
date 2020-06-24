package com.resfandiari.cafebazaar_market;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CafebazaarMarket {
    private final Activity activity;

    public CafebazaarMarket(
            final Activity activity
    ) {
        if (activity == null) {
            throw new IllegalStateException("No activity available!");
        }

        this.activity = activity;
    }

    public void referralToProgram() {
        try {
            String appPackageName = activity.getApplication().getPackageName();
            String uri = String.format("bazaar://details?id=%s",appPackageName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            intent.setPackage("com.farsitel.bazaar");
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e("cafebazaar","error when : opening application page on bazaar",e);
        }
    }

    public void referralToComment() {
        String bazaarPackageName = "com.farsitel.bazaar";
        String appPackageName = activity.getApplication().getPackageName();
        String uri = String.format("bazaar://details?id=%s",appPackageName);
        if (appPackageName != null) {
            try {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse(uri));
                intent.setPackage(bazaarPackageName);
                activity.startActivity(intent);
            } catch (Exception e) {
                Log.e("cafebazaar","error when : commenting on bazaar market",e);
            }
        }
    }

    public void referralToDeveloperPage(String developerId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=" + developerId));
            intent.setPackage("com.farsitel.bazaar");
            activity.startActivity(intent);
        }catch (Exception e) {
            Log.e("cafebazaar","error when : open developer page on bazaar market",e);
        }
    }

    public void referralToLogin() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://login"));
            intent.setPackage("com.farsitel.bazaar");
            activity.startActivity(intent);
        }catch (Exception e) {
            Log.e("cafebazaar","error when : login to bazaar market",e);
        }
    }

}

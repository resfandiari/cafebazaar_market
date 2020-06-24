package com.resfandiari.cafebazaar_market;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
    //    private final Activity activity;
    private final MethodChannel methodChannel;
    private final BinaryMessenger messenger;
    private @Nullable
    CafebazaarMarket cafebazaar;

    MethodCallHandlerImpl(
            Activity activity,
            BinaryMessenger messenger
    ) {
//        this.activity = activity;
        cafebazaar = new CafebazaarMarket(activity);

        this.messenger = messenger;

        methodChannel = new MethodChannel(messenger, "cafebazaarMarket");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "referralToProgram": {
                if (cafebazaar != null)
                    cafebazaar.referralToProgram();
                break;
            }
            case "referralToComment": {
                if (cafebazaar != null)
                    cafebazaar.referralToComment();
                break;
            }
            case "referralToDeveloperPage": {
                String developerId = call.argument("developerId");
                if (cafebazaar != null)
                    cafebazaar.referralToDeveloperPage(developerId);
                break;
            }
            case "referralToLogin": {
                if (cafebazaar != null)
                    cafebazaar.referralToLogin();
                break;
            }
            case "dispose": {
//                if (cafebazzar != null) {
//                    cafebazzar.dispose();
//                }
                result.success(null);
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    void stopListening() {
        methodChannel.setMethodCallHandler(null);
    }
}

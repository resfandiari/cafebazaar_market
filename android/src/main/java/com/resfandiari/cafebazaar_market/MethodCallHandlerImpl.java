package com.resfandiari.cafebazaar_market;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
    private final Activity activity;
    private final MethodChannel methodChannel;
    private final BinaryMessenger messenger;
    private @Nullable
    UpdateServiceConnection updateService;

    MethodCallHandlerImpl(
            Activity activity,
            BinaryMessenger messenger
    ) {
        this.activity = activity;
        updateService = new UpdateServiceConnection(activity);

        this.messenger = messenger;

        methodChannel = new MethodChannel(messenger, "cafebazaarMarket");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        switch (call.method) {
            case "referralToProgram": {
                CafebazaarMarket.referralToProgram(this.activity);
                break;
            }
            case "referralToComment": {
                CafebazaarMarket.referralToComment(this.activity);
                break;
            }
            case "referralToDeveloperPage": {
                String developerId = call.argument("developerId");
                CafebazaarMarket.referralToDeveloperPage(this.activity, developerId);
                break;
            }
            case "referralToLogin": {
                CafebazaarMarket.referralToLogin(this.activity);
                break;
            }
            case "isUpdateAvailable": {
                if (updateService != null)
                    updateService.checkForNewUpdate(
                            new Runnable() {
                                @Override
                                public void run() {
                                    result.success(true);
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {
                                    result.success(false);
                                }
                            });
                break;
            }
            case "dispose": {
                tearDown();
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    public void tearDown() {
        methodChannel.setMethodCallHandler(null);

        if (updateService != null)
            updateService.releaseService();

    }
}

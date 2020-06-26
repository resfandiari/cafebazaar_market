package com.resfandiari.cafebazaar_market;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener {
    // Debug tag, for logging
    static final String TAG = "MethodCallHandlerImpl";

    private final Activity activity;
    private final MethodChannel methodChannel;
    private final BinaryMessenger messenger;
    private @Nullable
    CafebazaarUpdateService updateService;
    private @Nullable
    CafebazaarPaymentService cafebazaarPaymentService;

    MethodCallHandlerImpl(
            Activity activity,
            BinaryMessenger messenger
    ) {
        this.activity = activity;
        updateService = new CafebazaarUpdateService(activity);
        cafebazaarPaymentService = new CafebazaarPaymentService(activity);

        this.messenger = messenger;

        methodChannel = new MethodChannel(messenger, "cafebazaarMarket");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final MethodChannel.Result result) {
        switch (call.method) {
            case "referralToProgram": {
                CafebazaarUtils.referralToProgram(this.activity);
                break;
            }
            case "referralToComment": {
                CafebazaarUtils.referralToComment(this.activity);
                break;
            }
            case "referralToDeveloperPage": {
                String developerId = call.argument("developerId");
                CafebazaarUtils.referralToDeveloperPage(this.activity, developerId);
                break;
            }
            case "referralToLogin": {
                CafebazaarUtils.referralToLogin(this.activity);
                break;
            }
            case "isUpdateAvailable": {
                try {
                    if (updateService != null)
                        updateService.checkForNewUpdate(result);

                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
                break;
            }
            case "initPay": {
                String rsaKey = call.argument("rsaKey");
                boolean debugMode = call.<Boolean>argument("debugMode");

                try {
                    if (cafebazaarPaymentService != null)
                        cafebazaarPaymentService.init(rsaKey, debugMode, result);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }
            //Purchasing Product
            case "launchPurchaseFlow": {
                String sku = call.argument("productKey");
                String payload = call.argument("payload");
                boolean consumption = call.<Boolean>argument("consumption");

                try {
                    if (cafebazaarPaymentService != null)
                        cafebazaarPaymentService.launchPurchaseFlow(sku, payload, consumption, result);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                break;
            }
            //Querying for Purchased Items
            case "getPurchase": {
                String sku = call.argument("sku");
                try {
                    if (cafebazaarPaymentService != null)
                        cafebazaarPaymentService.getPurchase(sku, result);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }
            //Request a list of products for sale
            case "queryInventoryAsync": {
                String sku = call.argument("sku");
                try {
                    if (cafebazaarPaymentService != null)
                        cafebazaarPaymentService.queryInventoryAsync(sku, result);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;

            }
            case "verifyDeveloperPayload": {
                try {
                    if (cafebazaarPaymentService != null)
                        cafebazaarPaymentService.verifyDeveloperPayload(result);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }


            case "dispose": {
                try {
                    disposePayment();
                    result.success(null);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    private void disposePayment() {
        if (cafebazaarPaymentService != null)
            cafebazaarPaymentService.onDestroy();
    }

    public void tearDown() {
        if (updateService != null)
            updateService.releaseService();

        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (cafebazaarPaymentService != null)
            cafebazaarPaymentService.activityResult(requestCode, resultCode, data);

        return false;
    }
}

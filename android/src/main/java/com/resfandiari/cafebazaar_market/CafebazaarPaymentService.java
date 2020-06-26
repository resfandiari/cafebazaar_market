package com.resfandiari.cafebazaar_market;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.resfandiari.cafebazaar_market.util.IabException;
import com.resfandiari.cafebazaar_market.util.IabHelper;
import com.resfandiari.cafebazaar_market.util.IabResult;
import com.resfandiari.cafebazaar_market.util.Inventory;
import com.resfandiari.cafebazaar_market.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodChannel;

public class CafebazaarPaymentService {
    // Debug tag, for logging
    static final String TAG = "CafebazaarPaymentService";
    //
    // SKUs for our products: the premium upgrade (non-consumable)
    static String SKU_PREMIUM = "";
    private String payLoad = "";

    // Does the user have the premium upgrade?
//    boolean mIsPremium = false;
//
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    IabHelper mHelper;


    private Activity activity;


    public CafebazaarPaymentService(
            Activity activity
    ) {
        if (activity == null) {
            throw new IllegalStateException("No activity available!");
        }
        this.activity = activity;
    }

    public void init(String rsaKey, boolean debugMode, final MethodChannel.Result pendingResult) {
// You can find it in your Bazaar console, in the Dealers section.
// It is recommended to add more security than just pasting it in your source code;
        mHelper = new IabHelper(this.activity, rsaKey);
        mHelper.enableDebugLogging(debugMode);

        Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    try {
                        pendingResult.error("Setup finished Error", "Problem setting up In-app Billing: " + result, null);
                    }catch (IllegalStateException e){
                        Log.e(TAG,e.getMessage());
                    }
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
//                mHelper.queryInventoryAsync( mGotInventoryListener(pendingResult));

                try {
                    pendingResult.success(true);
                }catch (IllegalStateException e){
                    Log.e(TAG,e.getMessage());
                }
            }
        });


    }

    public void onDestroy() {
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    public void launchPurchaseFlow(String productKey, final String payLoad, final boolean consumption
            , final MethodChannel.Result pendingResult) {
        Log.d(TAG, "launchPurchaseFlow");
        SKU_PREMIUM = productKey;

        mHelper.launchPurchaseFlow(activity, productKey, RC_REQUEST,
                OnIabPurchaseFinishedListener(consumption, pendingResult), payLoad);
    }

    public void getPurchase(String sku, final MethodChannel.Result pendingResult) {
        List<String> additionalSkuList = new ArrayList<>();
        additionalSkuList.add(sku);
        try {
            Purchase gasPurchase = mHelper.queryInventory(false, additionalSkuList).getPurchase(sku);
            if (gasPurchase != null) {
                pendingResult.success(gasPurchase.getOriginalJson());
            } else
                pendingResult.success(null);
        } catch (IabException e) {
            e.printStackTrace();
            pendingResult.error("get_purchase_error", e.getMessage(), null);
        }
    }

    public void queryInventoryAsync(String sku, final MethodChannel.Result pendingResult) {
        List<String> additionalSkuList = new ArrayList<>();
        additionalSkuList.add(sku);
        SKU_PREMIUM = sku;
        mHelper.queryInventoryAsync(true, additionalSkuList, mGotInventoryListener(pendingResult));
    }

    public void verifyDeveloperPayload(final MethodChannel.Result pendingResult) {
        pendingResult.success(payLoad);


    }

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener(final MethodChannel.Result pendingResult) {
        return new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                if (mHelper == null) return;
                // Log.d(TAG, "Query inventory finished.");
                if (result.isFailure()) {
                    // Log.d(TAG, "Failed to query inventory: " + result);
                    pendingResult.error("Inventory Listener Error", "Failed to query inventory: " + result, null);
                    return;
                }
                pendingResult.success(inventory.getSkuDetails(SKU_PREMIUM) + "");
                // Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            }
        };
    }


    private IabHelper.OnIabPurchaseFinishedListener OnIabPurchaseFinishedListener(final boolean consumption, final MethodChannel.Result pendingResult) {
        return new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                if (mHelper == null) return;
                JSONObject obj = new JSONObject();

                try {
                    if (result.isFailure()) {
                        Log.d(TAG, "Error purchasing: " + result);

                        obj.put("isFailure", result.isFailure());
                        obj.put("response", result.getResponse());
                        obj.put("message", result.getMessage());
                        obj.put("purchase", null);
                        pendingResult.success(obj.toString());
                        return;
                    }
                    Log.d(TAG, "Success purchasing: " + result);

                    obj.put("isSuccess", result.isSuccess());
                    obj.put("response", result.getResponse());
                    obj.put("message", result.getMessage());
                    obj.put("purchase", purchase.getOriginalJson());
                    payLoad = purchase.getDeveloperPayload();
                    pendingResult.success(obj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (consumption)
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener());
//                        if (purchase.getSku().equals(SKU_PREMIUM)) {
//                            // give user access to premium content and update the UI
//                        }
            }
        };
    }

//    public void consumingPurchase(String productKey){
//        SKU_PREMIUM = productKey;
//        mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener());
//    }

    // Called when consumption is complete
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener() {
        return new IabHelper.OnConsumeFinishedListener() {
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                // Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
                if (mHelper == null) return;
                if (result.isSuccess()) {
                    //  Log.d(TAG, "Consumption successful. Provisioning.");
                } else {
                    //   Log.d(TAG, "Error while consuming: " + result);
                }
                //  Log.d(TAG, "End consumption flow.");
            }
        };
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        // Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // Log.d(TAG, "onActivityResult " + data);
        } else {
            // Log.d(TAG, "onActivityResult handled by IABUtil." + data);
        }
    }
}

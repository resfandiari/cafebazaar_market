package com.resfandiari.cafebazaar_market;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * CafebazaarMarketPlugin
 */
public class CafebazaarMarketPlugin implements FlutterPlugin, ActivityAware {


    private static final String TAG = "CafebazaarMarketPlugin";
    private @Nullable
    FlutterPluginBinding flutterPluginBinding;
    private @Nullable
    MethodCallHandlerImpl methodCallHandler;

    public CafebazaarMarketPlugin() {
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        this.flutterPluginBinding = binding;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        this.flutterPluginBinding = null;
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects
    public static void registerWith(Registrar registrar) {
        if (registrar.activity() == null) {
            // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
            // we stop the registering process immediately because the CafebazaarMarket requires an activity.
            return;
        }
        CafebazaarMarketPlugin plugin = new CafebazaarMarketPlugin();
        plugin.maybeStartListening(
                registrar.activity(),
                registrar.messenger()
        );
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        maybeStartListening(
                binding.getActivity(),
                flutterPluginBinding.getBinaryMessenger()
        );
    }

    @Override
    public void onDetachedFromActivity() {
        if (methodCallHandler == null) {
            // Could be on too low of an SDK to have started listening originally.
            return;
        }

        methodCallHandler.tearDown();
        methodCallHandler = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    private void maybeStartListening(
            Activity activity,
            BinaryMessenger messenger
    ) {

        methodCallHandler =
                new MethodCallHandlerImpl(
                        activity, messenger);
    }
}
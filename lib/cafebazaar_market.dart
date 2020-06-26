import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CafebazaarMarket {
  static const MethodChannel _channel = const MethodChannel('cafebazaarMarket');

  //show app page on cafe bazaar
  static Future<Null> showProgramPage() async {
    await _channel.invokeMethod("referralToProgram");
    return null;
  }

  //add comment for app  on cafe bazaar
  static Future<Null> setComment() async {
    await _channel.invokeMethod("referralToComment");
    return null;
  }

  //show developer apps on cafe bazaar
  static Future<Null> showDeveloperPage(String developerId) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("developerId", () => developerId);
    await _channel.invokeMethod("referralToDeveloperPage", args);
    return null;
  }

  //show cafe bazaar login page
  static Future<Null> showCafebazzarLogin() async {
    await _channel.invokeMethod("referralToLogin");
    return null;
  }

  //check update available
  static Future<bool> isUpdateAvailable() async {
    final bool hasUpdate = await _channel.invokeMethod("isUpdateAvailable");
    return hasUpdate;
  }

  //init payment
  static Future<bool> initPay(
      {@required String rsaKey, bool debugMode = false}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("rsaKey", () => rsaKey);
    args.putIfAbsent("debugMode", () => debugMode);
    bool result = await _channel.invokeMethod("initPay", args);
    return result;
  }

  static Future<dynamic> launchPurchaseFlow(
      {@required String sku,
      bool consumption = false,
      String payload = ""}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("productKey", () => sku);
    args.putIfAbsent("payload", () => payload);
    args.putIfAbsent("consumption", () => consumption);
    dynamic result = await _channel.invokeMethod("launchPurchaseFlow", args);
    return jsonDecode(result);
  }

  static Future<dynamic> getPurchase({@required String sku}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("sku", () => sku);
    dynamic result = await _channel.invokeMethod("getPurchase", args);
    return jsonDecode(result);
  }

  static Future<dynamic> queryInventoryAsync({@required String sku}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("sku", () => sku);
    dynamic result = await _channel.invokeMethod("queryInventoryAsync", args);
    return result;
  }

  static Future<bool> verifyDeveloperPayload({@required String payload}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    String result = await _channel.invokeMethod("verifyDeveloperPayload", args);
    print(result);
    return result == payload;
  }

  static Future<Null> disposePayment() async {
    await _channel.invokeMethod("dispose");
    return null;
  }
}

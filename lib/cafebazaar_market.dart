import 'dart:async';

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

  static Future<Null> dispose() async {
    await _channel.invokeMethod("dispose");
    return null;
  }
}

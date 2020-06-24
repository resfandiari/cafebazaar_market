import 'dart:async';

import 'package:flutter/services.dart';

class CafebazaarMarket {
  static const MethodChannel _channel = const MethodChannel('cafebazaarMarket');

  static Future<Null> showProgramPage() async {
    await _channel.invokeMethod("referralToProgram");
    return null;
  }

  static Future<Null> setComment() async {
    await _channel.invokeMethod("referralToComment");
    return null;
  }

  static Future<Null> showDeveloperPage(String developerId) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("developerId", () => developerId);
    await _channel.invokeMethod("referralToDeveloperPage", args);
    return null;
  }

  static Future<Null> showCafebazzarLogin() async {
    await _channel.invokeMethod("referralToLogin");
    return null;
  }

  static Future<Null> dispose() async {
    await _channel.invokeMethod("dispose");
    return null;
  }
}

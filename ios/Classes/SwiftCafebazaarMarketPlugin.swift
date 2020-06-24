import Flutter
import UIKit

public class SwiftCafebazaarMarketPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "cafebazaar_market", binaryMessenger: registrar.messenger())
    let instance = SwiftCafebazaarMarketPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}

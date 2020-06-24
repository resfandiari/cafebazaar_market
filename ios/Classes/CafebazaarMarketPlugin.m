#import "CafebazaarMarketPlugin.h"
#if __has_include(<cafebazaar_market/cafebazaar_market-Swift.h>)
#import <cafebazaar_market/cafebazaar_market-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "cafebazaar_market-Swift.h"
#endif

@implementation CafebazaarMarketPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCafebazaarMarketPlugin registerWithRegistrar:registrar];
}
@end

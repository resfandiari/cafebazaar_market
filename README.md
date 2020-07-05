# cafebazaar_market  
  
flutter plugin for Cafebazaar android market (only works on android)  
  
## Getting Started  

### Communicate with Cafe Bazaar Application
ارتباط با برنامه کافه بازار

* ###  Go to Application page
 * رفتن به صفحه اپلیکیشن
```dart  
//launches application page on bazaar app if bazaar is installed  
CafebazaarMarket.showProgramPage();  
```  
  
* ###  Add Comment 
* گذاشتن کامنت و امتیاز دادن به برنامه 
```dart  
//launches comment bazaar dialog  
CafebazaarMarket.setComment();  
```  
  
* ###  Go to Developer page  
* رفتن به صفحه برنامه نویس 
```dart  
//launches developer page on bazaar  
CafebazaarMarket.showDeveloperPage("your_id");  
```  
  
* ###   Go to Cafe Bazaar login page  
* رفتن به صفحه ورود کافه بازار
```dart  
//launches bazaar login page  
CafebazaarMarket.showCafebazzarLogin();  
```  
### Check Application Update 
 بررسی اپدیت بودن برنامه
* ###  Checking for update (if newer version is available)  
* بررسی اپدیت بودن  برنامه در صورت موجود بودن نسخه جدید
```dart  
bool isUpdateAvailable = await CafebazaarMarket.isUpdateAvailable();  
 if(isUpdateAvailable) {  
 CafebazaarMarket.showProgramPage();  
 //or just show a dialog ask if user wants to update then call CafebazaarMarket.showProgramPage();  }  
```
### Cafe Bazaar Payment
بخش پرداخت کافه بازار
#### For use In-app purchases on your application use below code
* جهت استفاده از پرداخت درون برنامه ای به صورت زیر عمل کنید.

### 1. Updating Your Application's Manifest
* این خط رو به فایل مانیفست برنامه اظافه کنید
* Adding the com.farsitel.bazaar.permission.PAY_THROUGH_BAZAAR permission to your AndroidManifest.xml file
```dart
<uses-permission android:name="com.farsitel.bazaar.permission.PAY_THROUGH_BAZAAR" />
```

### 2. First of all initialize payment and rsa Key in `initState`
* این خط رو در قسمت initState اظافه کنید

use async and await
```dart
await CafebazaarMarket.initPay(rsaKey:"Your RSA Key From CafeBazaar");
```
### 3. And use this line in `dispose`
* این خط رو در قسمت dispose اظافه کنید
* Important: Remember to unbind from the In-app Billing service when you are done with your Activity. If you don’t unbind, the open service connection could cause your device’s performance to degrade. This example shows how to perform the unbind operation on a service connection to In-app Billing called mServiceConn by overriding the activity’s onDestroy method.
```dart
await CafebazaarMarket.disposePayment();
```

* ### To start a purchase request from your app, call the `launchPurchaseFlow` method on the In-app Billing plugin
```dart
Map<String,dynamic> result = await CafebazaarMarket.launchPurchaseFlow(
                        sku: "wm2", consumption: false,payload:"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
 // after pay you get some data from Cafebazaar, if you get response code -1005 the payment is canceled by the user and  if get code 0  the payment is Success

```
1. `sku` : your product id on CafeBazaar

2. `consumption` : if your product is not a subscriber type you must consume it, For product consumption, set the consumption to true

 *`consumption` : 
 زمانی که محصول شما از نوع اشتراکی نباشد و از نوع مصرفی باشد باید ان را مصرف کنید تا در خرید ها بعدی کاربر بتواند ان را دوباره خریداری کند برای مصرف یک محصول
 consumption 
 را به صورت 
 true 
 .تنظیم کنید

4.`payload` : The `developerPayload` String is used to specify any additional arguments that you want Bazaar to send back along with the purchase information.


* Result If pay is success
```dart
'{
    "isSuccess": "true",
    "response":"0",
    "message":"Success (response: 0:OK)",
    "purchase":{
       "orderId":"12999763169054705758.1371079406387615",
       "packageName":"com.example.app",
       "productId":"exampleSku",
       "purchaseTime":1345678900000,
       "purchaseState":0,
       "developerPayload":"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ",
       "purchaseToken":"rojeslcdyyiapnqcynkjyyjh"
     }
}'
````


* ### To get Purchase details use below code
```dart
Map<String,dynamic> result = CafebazaarMarket.getPurchase(sku:"your product sku") // you can find sku(product id) in your application in-app section
// you get the payment details if you consumption the product result is null
```

Result
```dart
'{
   "orderId":"12999763169054705758.1371079406387615",
   "packageName":"com.example.app",
   "productId":"exampleSku",
   "purchaseTime":1345678900000,
   "purchaseState":0,
   "developerPayload":"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ",
   "purchaseToken":"rojeslcdyyiapnqcynkjyyjh"
 }'
````

* Security Recommendation: When you send a purchase request, create a String token that uniquely identifies this purchase request and include this token in the developerPayload.You can use a randomly generated string as the token. When you receive the purchase response from Bazaar, make sure to check the returned data signature, the orderId, and the developerPayload String. For added security, you should perform the checking on your own secure server. Make sure to verify that the orderId is a unique value that you have not previously processed, and the developerPayload String matches the token that you sent previously with the purchase request.
* ### After Payment result check developerPayload result true or false
```dart
bool result = await CafebazaarMarket.verifyDeveloperPayload("your developerPayload");
```
#### For more information about Cofe Bazaar payment go to [Cafe Bazaar payment article ](https://developers.cafebazaar.ir/fa/docs/iab/).
#### Special thanks for payment section code from [cafebazaar_flutter](https://pub.dev/packages/cafebazaar_flutter) package.

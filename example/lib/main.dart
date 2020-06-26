import 'package:cafebazaar_market/cafebazaar_market.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _updateAvailable = 'Unknown';
  String _connectToBazaar = 'Unknown';

  @override
  void initState() {
    super.initState();
    //for payment
    initConnectToBazaar();
    initCheckUpdate();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initConnectToBazaar() async {
    String connectToBazaar;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      //check update available
      bool connect = await CafebazaarMarket.initPay(rsaKey: null);
      if (connect) {
        print("connect");
        connectToBazaar = "Yes";
      } else {
        print("disconnect");
        connectToBazaar = "No";
      }
    } on PlatformException {
      connectToBazaar = 'Failed to connect to Bazaar.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _connectToBazaar = connectToBazaar;
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initCheckUpdate() async {
    String pdateAvailable;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      //check update available
      bool hasUpdate = await CafebazaarMarket.isUpdateAvailable();
      if (hasUpdate) {
        print("has update");
        pdateAvailable = "Yes";
      } else {
        print("no update");
        pdateAvailable = "No";
      }
    } on PlatformException {
      pdateAvailable = 'Failed to get version for checking update';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _updateAvailable = pdateAvailable;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example Cafebazaar Market'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('Connect To Bazaar for payment: $_connectToBazaar\n'),
              Text('Update Available: $_updateAvailable\n'),
              SizedBox(
                height: 10,
              ),
              FlatButton(
                child: Text("page app"),
                color: Colors.black12,
                onPressed: () {
                  //show app page on cafe bazaar
                  CafebazaarMarket.showProgramPage();
                },
              ),
              SizedBox(
                height: 10,
              ),
              FlatButton(
                color: Colors.black12,
                child: Text("comment to app"),
                onPressed: () {
                  //add comment for app  on cafe bazaar
                  CafebazaarMarket.setComment();
                },
              ),
              SizedBox(
                height: 10,
              ),
              FlatButton(
                color: Colors.black12,
                child: Text("developer page"),
                onPressed: () {
                  //show developer apps on cafe bazaar
                  CafebazaarMarket.showDeveloperPage("1234");
                },
              ),
              SizedBox(
                height: 10,
              ),
              FlatButton(
                color: Colors.black12,
                child: Text("bazaar login page"),
                onPressed: () {
                  //show cafe bazaar login page
                  CafebazaarMarket.showCafebazzarLogin();
                },
              ),
              SizedBox(
                height: 10,
              ),
              RaisedButton(
                  child: Text("PayFirst"),
                  onPressed: () async {
                    Map<String, dynamic> result =
                        await CafebazaarMarket.launchPurchaseFlow(
                            sku: "PayFirst",
                            consumption: false,
                            payload:
                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                    print("resultresult $result");
                    CafebazaarMarket.verifyDeveloperPayload(
                            payload: "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ")
                        .then((res) {
                      print(res);
                    });
                  }),
              SizedBox(
                height: 10,
              ),
              RaisedButton(
                  child: Text("PaySecond"),
                  onPressed: () async {
                    Map<String, dynamic> result =
                        await CafebazaarMarket.launchPurchaseFlow(
                            sku: "PaySecond",
                            consumption: true,
                            payload: "yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                    print("resultresult $result");
                    CafebazaarMarket.verifyDeveloperPayload(
                            payload: "yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ")
                        .then((res) {
                      print(res);
                    });
                  }),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    //use dispose Payment if you use initPay
    CafebazaarMarket.disposePayment();
    super.dispose();
  }
}

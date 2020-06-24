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
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      //check update available
      bool hasUpdate = await CafebazaarMarket.isUpdateAvailable();
      if (hasUpdate) {
        print("has update");
      } else {
        print("no update");
      }
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
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
                child: Text("comment app"),
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
                child: Text("login page"),
                onPressed: () {
                  //show cafe bazaar login page
                  CafebazaarMarket.showCafebazzarLogin();
                },
              ),
              SizedBox(
                height: 10,
              ),
              Text('Running on: $_platformVersion\n')
            ],
          ),
        ),
      ),
    );
  }
}

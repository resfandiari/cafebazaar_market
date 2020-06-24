import 'package:cafebazaar_market/cafebazaar_market.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
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
                  CafebazaarMarket.showCafebazzarLogin();
                },
              ),
              SizedBox(
                height: 10,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

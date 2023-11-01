import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:herbdetectionandroid/src/home.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final cameras = await availableCameras();
  final firstCamera = cameras.first;

  runApp(MyApp(camera: firstCamera));
}

class MyApp extends StatelessWidget {
  final CameraDescription camera;

  MyApp({required this.camera});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(cameraDescription: camera),
    );
  }
}

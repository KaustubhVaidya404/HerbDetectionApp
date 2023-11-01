import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:camera/camera.dart';

class HomePage extends StatefulWidget {
  final CameraDescription cameraDescription;

  const HomePage({super.key, required this.cameraDescription});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  late CameraController cameraController;

  @override
  void initState() {
    super.initState();
    cameraController = CameraController(
      widget.cameraDescription,
      ResolutionPreset.high,
      enableAudio: false,
    );
    cameraController.initialize().then((_) {
      if (!mounted) {
        return;
      }
      setState(() {});
    });
  }

  @override
  void dispose() {
    cameraController.dispose();
    super.dispose();
  }

  void takePicture() async {
    if (!cameraController.value.isInitialized) {
      return;
    }

    final XFile xFile = await cameraController.takePicture();
    print('image received from camera');
  }

  void pickImage() async {
    final ImagePicker _imagePicker = ImagePicker();
    XFile? xFile = await _imagePicker.pickImage(source: ImageSource.gallery);

    if (xFile != null) {
      print('image received from gallary');
    }
  }

  @override
  Widget build(BuildContext context) {
    if (!cameraController.value.isInitialized) {
      return Container();
    }
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.green,
          title: Center(child: Text('Herb Detection')),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Flexible(flex: 1, child: Container()),
              AspectRatio(
                aspectRatio: cameraController.value.aspectRatio,
                child: CameraPreview(cameraController),
              ),
              ElevatedButton(
                onPressed: takePicture,
                child: const Text('Take Picture'),
              ),
              ElevatedButton(
                onPressed: pickImage,
                child: const Text('Pick Image from Gallery'),
              ),
              Flexible(flex: 1, child: Container()),
            ],
          ),
        ));
  }
}

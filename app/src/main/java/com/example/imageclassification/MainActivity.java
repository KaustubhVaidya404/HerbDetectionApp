package com.example.imageclassification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.tensorflow.lite.support.image.TensorImage;

import androidx.appcompat.app.AppCompatActivity;


import com.example.imageclassification.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button selectbtn,predictbtn;
    TextView result;
    Bitmap bitmap;
    ImageView imageview;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getPermissions();

        selectbtn = findViewById(R.id.selectimagebtn);
        predictbtn = findViewById(R.id.predictbtn);
        //captureimagebtn = findViewById(R.id.captureimagebtn);
        result = findViewById(R.id.result);
        imageview = findViewById(R.id.imageview);

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        predictbtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                try {
                    Model model = Model.newInstance(MainActivity.this);
                    //TensorBuffer.createFixedSize(new int[]{1, 180, 180, 3}, DataType.FLOAT32);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[] {1, 180, 180, 3}, DataType.FLOAT32);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());


                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    result.setText(getMax(outputFeature0.getFloatArray())+" ");

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    // TODO Handle the exception
                }



//                try {
//                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(MainActivity.this);
//
//                    // Creates inputs for reference.
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);
//                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
//
//                    // Runs model inference and gets result.
//                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
//                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//                    result.setText(getMax(outputFeature0.getFloatArray())+" ");
//
//                    // Releases model resources if no longer used.
//                    model.close();
//                } catch (IOException e) {
//                    // TODO Handle the exception
//                }


                // Initialization
//                ImageClassifierOptions options =
//                        ImageClassifierOptions.builder()
//                                .setBaseOptions(BaseOptions.builder().useGpu().build())
//                                .setMaxResults(1)
//                                .build();
//                ImageClassifier imageClassifier =
//                        ImageClassifier.createFromFileAndOptions(
//                                context, modelFile, options);
//
//// Run inference
//               result = imageClassifier.classify(image);

            }
        });
    }

    int getMax(float[] arg){
        int max = 0;
        for(int i=0; i<arg.length; i++){
            if(arg[i]>arg[max]){
                max = i;
            }
        }
        return max;
    }

//    void getPermissions() {
//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10){
            if(data!=null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);
                    imageview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
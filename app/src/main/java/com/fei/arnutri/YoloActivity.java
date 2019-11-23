package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.dnn.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class YoloActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    boolean startYolo = false;
    boolean firstTimeYolo = false;
    Button btnVoltar;
    Net tinyYolo;



    public void Yolo(View Button){

        if(startYolo == false) {

            startYolo = true;

            if(firstTimeYolo == false){

                firstTimeYolo = true;
                Context context = getApplicationContext();
                String tinyYoloCfg = context.getExternalFilesDir(null) + "/dnns/yolo-obj.cfg";
                String tinyYoloWeights = context.getExternalFilesDir(null) + "/dnns/yolo-obj_22000.weights";

                //String tinyYoloCfg = Environment.getExternalStorageDirectory() + "/dnns/yolo-obj.cfg";
                //String tinyYoloWeights = Environment.getExternalStorageDirectory() + "/dnns/yolo-obj_5000.weights";

                //String tinyYoloCfg = context.getExternalFilesDir("../java/com/fei/arnutri/yolo-obj.cfg").toString();
                //String tinyYoloWeights = Environment.getExternalStorageDirectory() + "/yolo-obj_5000.weights";
                //String tinyYoloWeights = context.getExternalFilesDir("../java/com/fei/arnutri/yolo-obj_5000.weights").toString();
                //String tinyYoloWeights = Environment.getExternalStorageDirectory() + "../java/com/fei/arnutri/yolo-obj_5000.weights";

                //String tinyYoloCfg = Environment.getExternalStorageDirectory() + "/yolo-obj.cfg";

                tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights);
            }

        } else {
            startYolo = false;
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yolo);


        btnVoltar = findViewById(R.id.btnVoltar);
        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YoloActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Cada frame é capturado e interceptado antes de ser exibido na tela
        Mat frame = inputFrame.rgba();

        if(startYolo == true){
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

            Mat imageBlob = Dnn.blobFromImage(frame, 0.00392, new Size(416,416), new Scalar(0,0,0),false, false);

            tinyYolo.setInput(imageBlob);
            java.util.List<Mat> result = new java.util.ArrayList<Mat>(3);

            List<String> outBlobNames = new java.util.ArrayList<>();
            outBlobNames.add(0, "yolo_82");
            outBlobNames.add(1, "yolo_94");
            outBlobNames.add(2, "yolo_106");

            tinyYolo.forward(result,outBlobNames);


            //pós processamento da saída
            float confThreshold = 0.3f;

            // Id das classes
            List<Integer> clsIds = new ArrayList<>();
            // Lista das confianças
            List<Float> confs = new ArrayList<>();
            // é possivel acessar as coordenadas das Bounding boxes
            List<Rect> rects = new ArrayList<>();

            // no caso do yolo o result terá o tamanho 3 , pois possui 3 layers
            for (int i =0; i < result.size(); ++i){

                Mat level = result.get(i);

                for(int j = 0; j < level.rows(); ++j){

                    Mat row = level.row(j);
                    Mat scores = row.colRange(5, level.cols());
                    Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                    float confidence = (float)mm.maxVal;

                    Point classIdPoint = mm.maxLoc;

                    if(confidence > confThreshold)
                    {
                        int centerX = (int) (row.get(0,0)[0] * frame.cols());
                        int centerY = (int) (row.get(0,1)[0] * frame.rows());
                        int width = (int) (row.get(0,2)[0] * frame.cols());
                        int height = (int) (row.get(0,3)[0] * frame.rows());

                        // o ponto mais a esquerda
                        int left = centerX - width/2;
                        // o ponto mais acima
                        int top = centerY - height/2;

                        clsIds.add((int)classIdPoint.x);
                        confs.add((float)confidence);

                        rects.add(new Rect(left, top, width, height));
                    }
                }
            }

            int ArrayLength = confs.size();

            if(ArrayLength >= 1){
                // aplicando o processo do non-maximum suppression, para evitar overlap de bboxes, pegando a que possui a maior certeza
                float nmsThresh = 0.2f;

                MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));

                Rect[] boxesArray = rects.toArray(new Rect[0]);
                MatOfRect boxes = new MatOfRect(boxesArray);
                MatOfInt indices = new MatOfInt();

                Dnn.NMSBoxes(boxes, confidences, confThreshold,nmsThresh, indices);

                // Desenha as boxes de resultado
                int[] ind = indices.toArray();

                for(int i = 0; i < ind.length; ++i){
                    int idx = ind[i];
                    Rect box = boxesArray[idx];
                    int idGuy = clsIds.get(idx);

                    float conf = confs.get(idx);

                    int intConf = (int) (conf * 100);



                    List<String> cocoNames = Arrays.asList("aveia","arroz_integral","feijao","ruffles","twix", "refrigerante_lata","bombom_ourobranco", "suco_laranja", "lays_churrasco","leite", "macarrao_integral");

                    List<String> badFoods = Arrays.asList("ruffles", "twix", "refrigerante_lata","bombom_ourobranco","lays_churrasco");

                    if(badFoods.contains(cocoNames.get(idGuy)) ){
                        Imgproc.putText(frame, cocoNames.get(idGuy)+" NAO COMPRE ",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2,new Scalar(255,0,0), 2);
                        //(255, 178, 50)
                        Imgproc.rectangle(frame,box.tl(), box.br(), new Scalar(255,0,0),2);


                        System.out.println(box);
                    }
                    else{
                        Imgproc.putText(frame, cocoNames.get(idGuy)+" " + intConf+"%(Certeza)",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2,new Scalar(0,255,0), 2);
                        //(255, 178, 50)
                        Imgproc.rectangle(frame,box.tl(), box.br(), new Scalar(0,255,0),2);


                        System.out.println(box);
                    }



                    /*
                    if(cocoNames.contains(cocoNames.get(idGuy))){
                        Imgproc.putText(frame, "Não compre",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2,new Scalar(255,0,0), 2);
                        Imgproc.rectangle(frame,box.tl(), box.br(), new Scalar(255,0,0),2);


                        System.out.println(box);
                    }
                    else{
                        Imgproc.putText(frame, cocoNames.get(idGuy)+" " + intConf+"%(Certeza)",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2,new Scalar(0,255,0), 2);
                        //(255, 178, 50)
                        Imgproc.rectangle(frame,box.tl(), box.br(), new Scalar(0,255,0),2);


                        System.out.println(box);
                    }

*/
                }
            }



        }

        return frame;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        if(startYolo == true) {

            Context context = this.getApplicationContext();
            String tinyYoloCfg = context.getExternalFilesDir("../java/com/fei/arnutri/yolo-obj.cfg").toString();
            //String tinyYoloWeights = Environment.getExternalStorageDirectory() + "/yolo-obj_5000.weights";
            String tinyYoloWeights = context.getExternalFilesDir("../java/com/fei/arnutri/yolo-obj_5000.weights").toString();
            //String tinyYoloWeights = Environment.getExternalStorageDirectory() + "../java/com/fei/arnutri/yolo-obj_5000.weights";

            //String tinyYoloCfg = Environment.getExternalStorageDirectory() + "/yolo-obj.cfg";




            tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights);

        }
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(), "Houve algum problema!", Toast.LENGTH_SHORT).show();
        } else {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }
}

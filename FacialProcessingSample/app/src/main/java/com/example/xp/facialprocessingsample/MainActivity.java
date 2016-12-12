package com.example.xp.facialprocessingsample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera.*;
import android.hardware.Camera;
import android.content.*;

import java.text.SimpleDateFormat;
import java.util.*;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import java.io.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.view.Display;

import com.qualcomm.snapdragon.sdk.face.*;


public class MainActivity extends AppCompatActivity implements Camera.PreviewCallback{
    Camera cameraObj;
    FrameLayout preview;
    Button switchButton, playButton;
    private CameraSurfacePreview mPreview;
    private int FRONT_CAMERA_INDEX = 1;
    private int BACK_CAMERA_INDEX = 0;
    ImageView adview, xview;
    public static MainActivity mactivity;
    private static boolean switchCamera = false;
    private boolean _qcSDKEnabled;
    FileAccess myFileAccess = null;
    FacialProcessing faceProc;
    Display display;
    long myTime = 0;
    boolean inFrame = false;
    private int displayAngle;
    int mode = 0;
    int myad = -1;
    int admode = 1;
    int adtime = 300;
    int nowx = 0, nowy = 0;

    private int numFaces;
    FaceData[] faceArray = null;

    DrawView drawView;

    final android.os.Handler handler1 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                default:
                    if(faceArray!= null && faceArray[msg.what] != null) {
                        TextView info = (TextView) findViewById(R.id.info);
                        info.setText("x:" + (float) (Math.round(faceArray[msg.what].getEyeGazePoint().x * 100)) / 100);
                        break;
                    }
            }
            super.handleMessage(msg);
        }
    };


    final android.os.Handler handler2 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                default:
                    if(faceArray!= null && faceArray[msg.what] != null) {
                        TextView info = (TextView) findViewById(R.id.info);
                        String st = (String) info.getText();
                        info.setText(st + "    y:" + (float) (Math.round(faceArray[msg.what].getEyeGazePoint().y * 100)) / 100);
                        break;
                    }
            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler3 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                default:
                     if(faceArray!= null && faceArray[msg.what] != null) {
                        TextView info = (TextView) findViewById(R.id.info);
                         String st = (String)info.getText();
                        info.setText(st + "    hor:" + faceArray[msg.what].getEyeHorizontalGazeAngle());
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler4 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    if (faceArray != null && faceArray[msg.what] != null){
                        TextView info = (TextView) findViewById(R.id.info);
                        String st = (String) info.getText();
                        info.setText(st + "    ver:" + faceArray[msg.what].getEyeVerticalGazeAngle());
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler5 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            ImageView adView = (ImageView)findViewById(R.id.ad);
            switch (msg.what) {
                default:
                    adView.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler6 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            ImageView adView = (ImageView)findViewById(R.id.ad);
            switch (msg.what){
                case 0:
                    adview.setImageResource(R.drawable.game1);
                    break;
                case 1:
                    adview.setImageResource(R.drawable.game2);
                    break;
                case 2:
                    adview.setImageResource(R.drawable.game3);
                    break;
                case 3:
                    adview.setImageResource(R.drawable.game4);
                    break;
                case 4:
                    adview.setImageResource(R.drawable.game5);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler7 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            ImageView adView = (ImageView)findViewById(R.id.ad);
            switch (msg.what){
                case 1:

                    break;
                case 2:
                    adview.setImageResource(R.drawable.game3);
                    break;
                case 3:
                    adview.setImageResource(R.drawable.game4);
                    break;
                case 4:
                    adview.setImageResource(R.drawable.game5);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    final android.os.Handler handler8 = new android.os.Handler(){
        public void handleMessage(Message msg) {
            ImageView picView = (ImageView)findViewById(R.id.imageView);
            nowx +=10;
            picView.setX(nowx);
            picView.setY(nowy);
            super.handleMessage(msg);
        }
    };

    private void startCamera(){
        _qcSDKEnabled = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);

        if(_qcSDKEnabled && faceProc == null){
            Toast.makeText(getApplicationContext(),"Supported",Toast.LENGTH_LONG).show();
            faceProc = FacialProcessing.getInstance();
        }
        else if(!_qcSDKEnabled){
            Toast.makeText(getApplicationContext(),"Not Supported",Toast.LENGTH_LONG).show();
        }


        if(!switchCamera) {
            cameraObj = Camera.open(FRONT_CAMERA_INDEX);
        }
        else{
            cameraObj = Camera.open(BACK_CAMERA_INDEX);
        }
        mPreview = new CameraSurfacePreview(MainActivity.this, cameraObj);
        preview = (FrameLayout)findViewById(R.id.camera_preview);
        preview.addView(mPreview);
       cameraObj.setPreviewCallback(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstaceState){

        mactivity = this;
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_main);
        preview = (FrameLayout)findViewById(R.id.camera_preview);
        startCamera();
        xview = (ImageView)findViewById(R.id.imageView);
        display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switchButton = (Button)findViewById(R.id.switchView);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 0) {
                    playButton.setVisibility(View.INVISIBLE);
                    VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    videoView.stopPlayback();
                    videoView.setVisibility(View.INVISIBLE);
                    ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
                    scrollView.setVisibility(View.VISIBLE);
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setVisibility(View.VISIBLE);
                    mode = 1;
                } else if (mode == 1) {
                    playButton.setVisibility(View.VISIBLE);
                    VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    videoView.setVisibility(View.VISIBLE);
                    ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
                    scrollView.setVisibility(View.INVISIBLE);
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                    mode = 0;
                }
            }
        });
        adview = (ImageView)findViewById(R.id.ad);
        Random rand = new Random();
        myad = rand.nextInt(5);
        Message message6 = new Message();
        message6.what = myad;
        handler6.sendMessage(message6);
        adtime = rand.nextInt(200);
        adtime += 200;
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
        Date date=new Date();
        myFileAccess = new FileAccess();
        Log.i("666", "233");
        myFileAccess.initFile(adtime + "_" + myad);

        playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.INVISIBLE);
                playVideo();

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                myTime += 1;
                Log.i("666", myTime +"_"+adtime);

                if (myTime == adtime) {
                    Message message5 = new Message();
                    message5.what = admode;
                    Log.i("666", "0");
                    handler5.sendMessage(message5);
                }

                if(myTime % 10 ==0){
                    Message message8 = new Message();
                    message8.what = 0;
                    handler5.sendMessage(message8);
                }

                if (faceArray == null)
                {
                    return;
                }
                if (inFrame) {
                    for (int i = 0; i < faceArray.length; i++) {
                        if (faceArray[i] != null && faceArray[i].getEyeGazePoint() != null) {
                            Message message1 = new Message();
                            message1.what = i;
                            Message message2 = new Message();
                            message2.what = i;
                            Message message3 = new Message();
                            message3.what = i;
                            Message message4 = new Message();
                            message4.what = i;
                            handler1.sendMessage(message1);
                            handler2.sendMessage(message2);
                            handler3.sendMessage(message3);
                            handler4.sendMessage(message4);


                            myFileAccess.FileWriteLog(String.valueOf(faceArray[i].getEyeGazePoint().x) + "|" + String.valueOf(faceArray[i].getEyeGazePoint().y) + "|" + String.valueOf(faceArray[i].getEyeHorizontalGazeAngle()) + "|" + String.valueOf(faceArray[i].getEyeVerticalGazeAngle())
                                    + "|" + String.valueOf(myTime));
                        }
                    }
                }
            }
        }, 1000, 100);
}

    @Override
    public void onPreviewFrame(byte[] data, Camera camera){

        int surfaceWidth = mPreview.getWidth();
        int surfaceHeight = mPreview.getHeight();
        faceProc.normalizeCoordinates(surfaceWidth, surfaceHeight);

        numFaces = faceProc.getNumFaces();
        //numFaces = 0;
        if(numFaces > 0){
            Log.d("TAG", "Face Detected");
            faceArray = faceProc.getFaceData();
            preview.removeView(drawView);
            drawView = new DrawView(this, faceArray, true, this);
            inFrame = true;
            preview.addView(drawView);
        }
        else{
            preview.removeView(drawView);
            drawView = new DrawView(this, null, false, this);
            inFrame = false;
            preview.addView(drawView);
        }

        int dRotation = display.getRotation();
        FacialProcessing.PREVIEW_ROTATION_ANGLE angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;
        switch(dRotation){
            case 0:
                displayAngle = 90;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90;
                break;

            case 1:
                displayAngle = 0;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;
                break;

            case 2:
                displayAngle = 270;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_270;
                break;

            case 3:
                displayAngle = 180;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_180;
                break;
        }

        cameraObj.setDisplayOrientation(displayAngle);

        if(_qcSDKEnabled){
            if(faceProc == null){
                faceProc = FacialProcessing.getInstance();
            }
            Parameters params = cameraObj.getParameters();
            android.hardware.Camera.Size previewSize = params.getPreviewSize();

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !switchCamera){
                faceProc.setFrame(data, previewSize.width, previewSize.height, true, angleEnum);
            }
            else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && switchCamera){
                faceProc.setFrame(data, previewSize.width, previewSize.height, false, angleEnum);
            }
            else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !switchCamera){
                faceProc.setFrame(data, previewSize.width, previewSize.height, true, angleEnum);
            }
            else{
                faceProc.setFrame(data, previewSize.width, previewSize.height, false, angleEnum);
            }
        }
    }

    private void stopCamera(){
        if(cameraObj != null){
            cameraObj.stopPreview();
            cameraObj.setPreviewCallback(null);

            preview.removeView(mPreview);
            cameraObj.release();
            if(_qcSDKEnabled){
                faceProc.release();
                faceProc = null;
            }
        }
        cameraObj = null;
    }

    protected void onPause(){
        super.onPause();
        //stopCamera();
    }

    protected void playVideo(){
        VideoView videoView=(VideoView)findViewById(R.id.videoView);
        videoView.setVideoPath(android.os.Environment.getExternalStorageDirectory().getPath() + "/ha.wmv");
        videoView.requestFocus();
        videoView.start();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    protected void onResume(){
        super.onResume();
        if(cameraObj != null){
            stopCamera();
        }
        startCamera();
    }

}


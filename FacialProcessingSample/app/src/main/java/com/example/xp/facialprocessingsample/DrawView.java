package com.example.xp.facialprocessingsample;
import android.hardware.camera2.params.Face;
import android.view.*;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.*;
import android.view.Display;
import android.graphics.*;
import android.graphics.PorterDuff.*;

import com.qualcomm.snapdragon.sdk.face.*;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by XP on 16/4/24.
 */


public class DrawView extends SurfaceView{
    public FaceData []mFaceArray;
    boolean _inFrame;
    TextView info;
    private Paint leftEyeBrush = new Paint();
    private Paint rightEyeBrush = new Paint();
    private Paint mouthBrush = new Paint();
    private Paint rectBrush = new Paint();
    public Point leftEye, rightEye, mouth;
    Rect mFaceRect;
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    FileAccess myFileAccess;
    Timer timer;
    MainActivity ma;
    long mytime;
    public DrawView(Context context, FaceData []FaceArray, boolean inFrame, MainActivity _ma){
        super(context);
        setWillNotDraw(false);

        mFaceArray = FaceArray;
        _inFrame = inFrame;
        myFileAccess = new FileAccess();
        ma = _ma;
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(mFaceArray == null){
            return;
        }
        if(_inFrame){
            for(int i = 0; i < mFaceArray.length; i++){
                leftEyeBrush.setColor(Color.RED);
                canvas.drawCircle(mFaceArray[i].leftEye.x, mFaceArray[i].leftEye.y, 5f, leftEyeBrush);

                rightEyeBrush.setColor(Color.GREEN);

                canvas.drawCircle(mFaceArray[i].rightEye.x, mFaceArray[i].rightEye.y, 5f, rightEyeBrush);
                System.out.println(mFaceArray[i].getEyeGazePoint().x);
                System.out.println(mFaceArray[i].getEyeGazePoint().y);
                //info = (TextView)findViewById(R.id.info);
                //info.setText(String.valueOf(mFaceArray[i].getEyeGazePoint().x));
                android.util.Log.e("pointlog_x", String.valueOf(mFaceArray[i].getEyeGazePoint().x) + "|" + String.valueOf(mFaceArray[i].getEyeGazePoint().y)
                        + "|" + String.valueOf(ma.myTime));

                //myFileAccess.FileWriteLog(String.valueOf(mFaceArray[i].getEyeGazePoint().x) + "|" + String.valueOf(mFaceArray[i].getEyeGazePoint().y)
                   //     + "|" + String.valueOf(mytime));


                //canvas.drawCircle(mFaceArray[i].getEyeGazePoint().x, mFaceArray[i].getEyeGazePoint().y, 10f, mouthBrush);

                setRectColor(mFaceArray[i], rectBrush);
                rectBrush.setStrokeWidth(2);
                rectBrush.setStyle(Paint.Style.STROKE);
                canvas.drawRect(mFaceArray[i].rect.left, mFaceArray[i].rect.top, mFaceArray[i].rect
                        .right, mFaceArray[i].rect.bottom, rectBrush);



            }
        }
        else{
            canvas.drawColor(0, Mode.CLEAR);
        }
    }

    private void setRectColor(FaceData faceData, Paint rectBrush){
        if(faceData.getSmileValue() < 40){
            rectBrush.setColor(Color.RED);
        }
        else if(faceData.getSmileValue() < 55){
            rectBrush.setColor(Color.parseColor("#FE642E"));
        }
        else if(faceData.getSmileValue() < 70){
            rectBrush.setColor(Color.parseColor("#D7DF01"));
        }
        else if(faceData.getSmileValue() < 85){
            rectBrush.setColor(Color.parseColor("#86B404"));
        }
        else{
            rectBrush.setColor(Color.parseColor("#5FB404"));
        }
    }


}

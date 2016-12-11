package com.example.xp.facialprocessingsample;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.content.*;

/**
 * Created by XP on 16/4/24.
 */
public class CameraSurfacePreview extends SurfaceView implements
        SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private Camera mCamera;
    Context mContext;

    public CameraSurfacePreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mContext = context;
        // Install a SurfaceHolder.Callback so we get notified when the underlying  surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.rnHolder.^e^Pyfie(5urfaceHplder.5URFACE_TYPE_PU5H_BUFFER5)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder argO, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch (java.io.IOException e) {
            android.util.Log.d("TAG", "Error setting camera preview;" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
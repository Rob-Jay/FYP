package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    String filepath = null;
    private FloatingActionButton btnCapture ;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "CameraActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().hide();
        textureView = (TextureView) findViewById(R.id.view_finder);
        btnCapture  = findViewById(R.id.btnCapture );
        System.out.println("On Create Runs");
        if (allPermissionGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    //settings for camera when Activity starts
    private void startCamera() {
        Log.d(TAG, "startActivity for Camera is running");
        System.out.println("Start Camera runs");
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());
        PreviewConfig pConfig = new PreviewConfig.Builder().setLensFacing(CameraX.LensFacing.BACK).setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        Log.d(TAG, "onUpdated for CameraActivity is running");
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);
                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        //On click listener for button Capture
        System.out.println("before Set On click listener");
        btnCapture .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { System.out.println("onClick listener");
                Log.d(TAG, "onClick for CameraActivity is running");
                imgCap.takePicture(new ImageCapture.OnImageCapturedListener()
                {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees)
                    {
                        System.out.println("inside onCapture Sucess");
                        Bitmap bitmap = textureView.getBitmap();
                        ProcessBitmap(bitmap);
                        finish();
                    }

                });
            }
        });    CameraX.bindToLifecycle((LifecycleOwner)this, preview, imgCap);

        }

        //Process Bitmap to the next activity.
        //Method creates a png file and compresses the bitmap into the png
    //this is to allow data to be passed between activitys.
        public void ProcessBitmap(Bitmap bitmap){
            try {
                Log.d(TAG, "Process Bitmap for CameraActivity is running");

                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //Cleanup
                stream.close();
                bitmap.recycle();
                //Pop intent
                Intent intent = new Intent(this, SummarizeActivity.class);
                intent.putExtra("image", filename);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }






    //Checks for Camera permissions
    private boolean allPermissionGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "No permissions given for camera activity");

                return false;
            }
        }
        return true;
    }

    private void updateTransform() {
        Log.d(TAG, "UpdateTransformer for CameraActivity is running");
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();
        float cX = w / 2f;
        float cY = h / 2f;
        int rotationDgr;
        int rotation = (int) textureView.getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }
        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private Bitmap imageProxyToBitmap(ImageProxy image)
    {
        Log.d(TAG, "imageProxyToBitmap for CameraActivity is running");
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}


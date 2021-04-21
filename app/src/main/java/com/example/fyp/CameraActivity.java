package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* Camera Activity can be accessed from the Main and Summarize activity.
The CamerActivity is used to take an Image/ bitmap. This bitmap is used to extract text in the Summarize Activity*/
public class CameraActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private TextureView textureView;
    private String filepath = null;
    private ImageView toolbarBack;
    private ImageView btnCapture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "CameraActivity";
    private static String KEY_SUMMARY_TEXT = "SUMMARYTEXT";
    private static String KEY_SCAN_TEXT = "SCANTEXT";
    private Switch flashLight;
    private boolean flash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate for Camera is running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar();

        //Locate all enteties from XML file
        textureView = findViewById(R.id.view_finder);
        btnCapture = findViewById(R.id.btnCapture);
        flashLight = findViewById(R.id.flashLight);
        toolbarBack = findViewById(R.id.toolbar_back);
// initalize flash as false
        flash = false;
        System.out.println("On Create Runs");

        //Application needs camera permission to run
        if (allPermissionGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        //On clicklistener for toolbar
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                back(view);
            }
        });
    }

    //settings for camera when Activity starts
    private void startCamera() {
        Log.d(TAG, "startActivity for Camera is running");
        CameraX.unbindAll();

        //Setting ratio and view for camera view. Currntly matches the aspect ratio of device
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());

        //Preview config is what the camera sees before clicking capture button
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

        //Setting image capture config to MIN_LATENCY for fast caption of Image with high quality
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        //On click listener for button Capture
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            //Button to click and capture photo and turn torch back off. This call process bitmap to pass the image to the next activity
            public void onClick(View view) {
                System.out.println("onClick listener");
                Log.d(TAG, "onClick for CameraActivity is running");
                imgCap.takePicture(new ImageCapture.OnImageCapturedListener() {
                    @Override
                    public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
                        System.out.println("inside onCapture Success");
                        preview.enableTorch(false);
                        Bitmap bitmap = textureView.getBitmap();
                        ProcessBitmap(bitmap);
                        finish();
                    }

                });
            }
        });
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, imgCap);

        //check on changed listener to seel if flash is switched on
        flashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flash = true;
                    flashLight.setText("Flash On");
                } else {
                    flash = false;
                    flashLight.setText("Flash Off");
                }
                preview.enableTorch(flash);
            }
        });
    }

    //Pass bitmap to Summarize activity This method compresses the Image so it can be passed and not break the 1mb limit
    public void ProcessBitmap(Bitmap bitmap) {
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
            String scan_text = getIntent().getStringExtra(KEY_SCAN_TEXT);
            String summary_text = getIntent().getStringExtra(KEY_SUMMARY_TEXT);
            Intent intent = new Intent(this, SummarizeActivity.class);

            intent.putExtra("image", filename);
            intent.putExtra(KEY_SUMMARY_TEXT, scan_text);
            intent.putExtra(KEY_SCAN_TEXT, summary_text);
            startActivity(intent);
            finish();

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


    //Rotation update. This checks the current position of the camera and corrects it so bitmap can be processed and read correctly
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

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        Log.d(TAG, "imageProxyToBitmap for CameraActivity is running");
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    //Method opens default camera on some devices
    public void ChangeCamera(View view) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        this.startActivity(intent);
    }

    //sends user to summarize activity
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), SummarizeActivity.class));
        finish();
    }
}


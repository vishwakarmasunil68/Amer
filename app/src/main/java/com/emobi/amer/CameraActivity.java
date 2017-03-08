package com.emobi.amer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private static final String LAUNCH_BUTTON_LOCATION = "launch_button_location";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;

    private static final int CAMERA_45_DEGREE = 45;
    private static final int CAMERA_90_DEGREE = 90;
    private static final int CAMERA_180_DEGREE = 180;
    private static final int CAMERA_270_DEGREE = 270;
    private static final int CAMERA_360_DEGREE = 360;
    private static final int DELAY_500_DURATION = 500;
    private static final int DELAY_600_DURATION = 600;
    final String TAG="camera";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private Camera mCamera;
    private CameraPreview mPreview;
    private TextView camera_btn_close;
    private ImageView camera_btn_switch;
    FrameLayout preview;
    int currentCameraId=0;
    boolean take_picture=true;
    public static Bitmap bitmap;
    ImageView iv_carpet_image;
    FrameLayout frame_main;
    RelativeLayout rv_top_layout,camera_footer,relative_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frame_main= (FrameLayout) findViewById(R.id.frame_main);
        rv_top_layout= (RelativeLayout) findViewById(R.id.rv_top_layout);
        relative_main= (RelativeLayout) findViewById(R.id.relative_main);
        camera_footer= (RelativeLayout) findViewById(R.id.camera_footer);
        camera_btn_close= (TextView) findViewById(R.id.camera_btn_close);
        iv_carpet_image= (ImageView) findViewById(R.id.iv_carpet_image);
        camera_btn_switch= (ImageView) findViewById(R.id.camera_btn_switch);
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        FloatingActionButton captureButton = (FloatingActionButton) findViewById(R.id.camera_capture_btn);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                    rv_top_layout.setVisibility(View.GONE);
//                        camera_footer.setVisibility(View.GONE);
//                        Bitmap bitmap=viewToBitmap(frame_main);
//                        SaveBitmap(bitmap);

                        // get an image from the camera

//                        int[] location = new int[2];
//                        iv_carpet_image.getLocationOnScreen(location);
//                        int x = location[0];
//                        int y = location[1];
//                        Log.d(TAG,"x:-"+x+",y:-"+y);
                        if(take_picture) {
                            take_picture=false;
                            if(mCamera!=null) {
                                mCamera.takePicture(null, null, mPicture);
                            }
                            else{

                            }
                        }
                        else{

                        }
//                        startActivity(new Intent(CaptureProductActivity.this,ProductCroperActivity.class));
//                        finish();
                    }
                }
        );
        try {
            mCamera.setDisplayOrientation(90);
        }
        catch (Exception e){

        }

        camera_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        camera_btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mCamera!=null){
                    mCamera.release();
                }
                if(currentCameraId==0) {
                    preview.removeAllViews();
                    mCamera = getFrontCameraInstance();
                    mPreview = new CameraPreview(CameraActivity.this, mCamera);
                    preview.addView(mPreview);
                    mCamera.setDisplayOrientation(90);
                }
                else{
                    preview.removeAllViews();
                    mCamera = getCameraInstance();
                    mPreview = new CameraPreview(CameraActivity.this, mCamera);
                    preview.addView(mPreview);
                    mCamera.setDisplayOrientation(90);
                }
            }
        });

    }
    public Bitmap viewToBitmap(FrameLayout frameLayout) {
        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache(true);
        return Bitmap.createBitmap(frameLayout.getDrawingCache());
    }
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2,float left,float top) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, left, top, null);
        return bmOverlay;
    }
    public void SaveBitmap(Bitmap bitmap){
        try {
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/savebitmap.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
            Log.d(TAG,"bitmap saved");
            finish();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rv_top_layout.setVisibility(View.VISIBLE);
        camera_footer.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        take_picture=true;
    }

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            currentCameraId=0;
            c = Camera.open(currentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    public Camera getFrontCameraInstance(){
        Camera c = null;
        try {
            currentCameraId=1;
            c = Camera.open(currentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            BitmapDrawable drawable = (BitmapDrawable) iv_carpet_image.getDrawable();
            Bitmap bitmap2 = drawable.getBitmap();
            int[] location = new int[2];
            iv_carpet_image.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            Log.d(TAG,"x:-"+x+",y:-"+y);
            Bitmap bmp=overlay(bitmap,bitmap2,x,y);
//            bitmap=Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()-(bitmap.getWidth()/6),bitmap.getHeight()-(bitmap.getHeight()/6), true);
            SaveBitmap(bmp);
//            getImageCaptured

//            Intent i = new Intent(CameraActivity.this, ProductCroperActivity.class);
//            i.putExtra("BitmapImage", bitmap);
//            startActivity(i);



//            String path= Environment.getExternalStorageDirectory()+File.separator+"filmfans";
//            File dir=new File(path);
//            dir.mkdirs();
//            File pictureFile = new File(path+File.separator+"takenpic.png");
//            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions: ");
//                return;
//            }
//
//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                Log.d(TAG, "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d(TAG, "Error accessing file: " + e.getMessage());
//            }
        }
    };
}

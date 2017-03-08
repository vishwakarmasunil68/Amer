package com.emobi.amer.camerapack;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.emobi.amer.R;
import com.jraska.falcon.Falcon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraCPActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    @BindView(R.id.texture_view)
    TextureView mTextureView;
    @BindView(R.id.btn_click)
    Button btn_click;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        mTextureView.setSurfaceTextureListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(mTextureView);
//        ImageView imageView=new ImageView(this);
//        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(params);
//        imageView.setImageResource(R.mipmap.ic_launcher);
//        mTextureView.add
        imageView.setImage(ImageSource.resource(R.mipmap.ic_launcher));
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    SaveBitmap(mTextureView.getBitmap());
//                SaveBitmap(viewToBitmap(framelayout));
                Falcon.takeScreenshot(CameraCPActivity.this, new File(Environment.getExternalStorageDirectory()+"/amer/screen.png"));
            }
        });
    }
    private final String TAG=getClass().getSimpleName();
    public void SaveBitmap(Bitmap bitmap){
        try {
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/amer/savebitmap.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
            Log.d(TAG,"bitmap saved");
            finish();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        rv_top_layout.setVisibility(View.VISIBLE);
//        camera_footer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER));

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException t) {
        }

        mCamera.startPreview();
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // make any resize, rotate or reformatting changes here
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

            mCamera.setDisplayOrientation(90);

        } else {

            mCamera.setDisplayOrientation(0);

        }
        // start preview with new settings
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    public Bitmap viewToBitmap(FrameLayout frameLayout) {
        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache(true);
        return Bitmap.createBitmap(frameLayout.getDrawingCache());
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, the Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
    }
}

//    @BindView(R.id.mySurfaceView)
//    FrameLayout mySurfaceView;
//    @BindView(R.id.btn_click)
//    Button btn_click;
//    @BindView(R.id.camera_view)
//    CapturePreview camera_view;
//
//
//    private final String TAG=getClass().getSimpleName();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera_cp);
//        ButterKnife.bind(this);
//
//        btn_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Bitmap bitmap=viewToBitmap(mySurfaceView);
////                SaveBitmap(bitmap);
//                TakeScreenshot();
//            }
//        });
//    }
//
//    public Bitmap viewToBitmap(FrameLayout frameLayout) {
//        frameLayout.setDrawingCacheEnabled(true);
//        frameLayout.buildDrawingCache(true);
//        return Bitmap.createBitmap(frameLayout.getDrawingCache());
//    }
//    public void SaveBitmap(Bitmap bitmap){
//        try {
//            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/amer/savebitmap.png");
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
//            output.close();
//            Log.d(TAG,"bitmap saved");
//            finish();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        rv_top_layout.setVisibility(View.VISIBLE);
////        camera_footer.setVisibility(View.VISIBLE);
//    }
//    public void TakeScreenshot(){    //THIS METHOD TAKES A SCREENSHOT AND SAVES IT AS .jpg
//        Random num = new Random();
//        int nu=num.nextInt(1000); //PRODUCING A RANDOM NUMBER FOR FILE NAME
////        camera_view.setDrawingCacheEnabled(true); //CamView OR THE NAME OF YOUR LAYOUR
////        camera_view.buildDrawingCache(true);
////        Bitmap bmp = Bitmap.createBitmap(camera_view.getDrawingCache());
////        camera_view.setDrawingCacheEnabled(false); // clear drawing cache
//
//        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        camera_view.draw(c);
//        SaveBitmap(b);
////        ByteArrayOutputStream bos = new ByteArrayOutputStream();
////        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
////        byte[] bitmapdata = bos.toByteArray();
////        ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);
////
////        String picId=String.valueOf(nu);
////        String myfile="Ghost"+picId+".jpeg";
////
////        File dir_image = new  File(Environment.getExternalStorageDirectory()+//<---
////                File.separator+"Ultimate Entity Detector");          //<---
////        dir_image.mkdirs();                                                  //<---
////        //^IN THESE 3 LINES YOU SET THE FOLDER PATH/NAME . HERE I CHOOSE TO SAVE
////        //THE FILE IN THE SD CARD IN THE FOLDER "Ultimate Entity Detector"
////
////        try {
////            File tmpFile = new File(dir_image,myfile);
////            FileOutputStream fos = new FileOutputStream(tmpFile);
////
////            byte[] buf = new byte[1024];
////            int len;
////            while ((len = fis.read(buf)) > 0) {
////                fos.write(buf, 0, len);
////            }
////            fis.close();
////            fos.close();
////            Toast.makeText(getApplicationContext(),
////                    "The file is saved at :SD/Ultimate Entity Detector",Toast.LENGTH_LONG).show();
////            bmp1 = null;
////            camera_image.setImageBitmap(bmp1); //RESETING THE PREVIEW
////            camera.startPreview();             //RESETING THE PREVIEW
//
//    }
//}

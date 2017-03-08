package com.emobi.amer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.emobi.amer.multitouch.MultiTouchListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasicActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{
    @BindView(R.id.texture_view)
    TextureView texture_view;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.relative_view)
    RelativeLayout relative_view;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    private Camera mCamera;
    private final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String image_url=bundle.getString("image_url");
            Log.d(TAG,"url:-"+image_url);
            Picasso.with(getApplicationContext()).load(image_url).into(imageView1);
        }
        else{
            finish();
        }

        texture_view.setSurfaceTextureListener(this);
        imageView1.setOnTouchListener(new MultiTouchListener());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mergeBitmaps();
            }
        });
    }
    public void mergeBitmaps(){
        button1.setVisibility(View.GONE);
        relative_view.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        Bitmap carpet=takeScreenshot(relative_view,Bitmap.Config.ARGB_8888);
        button1.setVisibility(View.VISIBLE);
        Bitmap newImage=overlay(texture_view.getBitmap(),carpet);
        SaveBitmap(newImage);
    }
    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    public void SaveBitmap(Bitmap bmp){
        FileOutputStream out = null;
        try {
            String path = Environment.getExternalStorageDirectory()+File.separator+"DCIM"+File.separator+"Camera";
            Log.d(TAG,"camera path:-"+path);
            if(new File(path).exists()){

            }
            else{
                path=Environment.getExternalStorageDirectory()+File.separator+"sc";
                File dir=new File(Environment.getExternalStorageDirectory()+File.separator+"sc");
                dir.mkdirs();
            }

            File f=new File(path+File.separator+System.currentTimeMillis()+".png");
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            Log.d(TAG,"save bitmap");
//            openGallery(f);
            Intent intent=new Intent(BasicActivity.this, ImagePreviewActivity.class);
            intent.putExtra("image_url",f.toString());
            startActivity(intent);
//            Uri URI = Uri.parse(f.toString());
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(f), "image/*");
//            startActivity(intent);
//            finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void openGallery(File f){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.emobi.amer.fileProvider", f);
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(f), type);
            }
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getApplicationContext(), "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera = Camera.open();

        try {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);
        } catch (IOException ioe) {
            // Something bad happened
        }
    }
    public static Bitmap takeScreenshot(View view, Bitmap.Config quality) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), quality);
        Canvas canvas = new Canvas(bitmap);

        Drawable backgroundDrawable = view.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT);
        }
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}

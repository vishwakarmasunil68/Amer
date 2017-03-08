package com.emobi.amer.camerapack;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.emobi.amer.ImagePreviewActivity;
import com.emobi.amer.R;
import com.emobi.amer.multitouch.MultiTouchListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback
{
    private Camera camera = null;
    private SurfaceView cameraSurfaceView = null;
    private SurfaceHolder cameraSurfaceHolder = null;
    private boolean previewing = false;
    RelativeLayout relativeLayout;

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.relative_view)
    RelativeLayout relative_view;



    private Button btnCapture = null;
    private final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);



        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main2);
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

        relativeLayout=(RelativeLayout) findViewById(R.id.containerImg);
        relativeLayout.setDrawingCacheEnabled(true);
        cameraSurfaceView = (SurfaceView)
                findViewById(R.id.surfaceView1);
        //  cameraSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(640, 480));
        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.addCallback(this);
        //    cameraSurfaceHolder.setType(SurfaceHolder.
        //                                               SURFACE_TYPE_PUSH_BUFFERS);



        imageView1.setOnTouchListener(new MultiTouchListener());
        btnCapture = (Button)findViewById(R.id.button1);
        btnCapture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
//                camera.takePicture(cameraShutterCallback,
//                    cameraPictureCallbackRaw,
//                    cameraPictureCallbackJpeg);
                takeshoot();
//                DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
//                int width = metrics.widthPixels;
//                int height = metrics.heightPixels;
//                Bitmap bitmap=SavePixels(0,0,width,height,cameraSurfaceView);
//                Log.d(TAG,"width:-"+imageView1.getWidth());
//                Log.d(TAG,"height:-"+imageView1.getHeight());
//                Bitmap bitmap=takeScreenshot(relative_view,Bitmap.Config.ARGB_8888);
//                SaveBitmap(bitmap);

            }
        });
    }
    public void takeshoot(){
        Process sh = null;
        try {
            sh = Runtime.getRuntime().exec("su", null,null);
        OutputStream os = sh.getOutputStream();
        os.write(("/system/bin/screencap -p " + Environment.getExternalStorageDirectory()+File.separator+"sc"+File.separator+System.currentTimeMillis()+".png").getBytes("ASCII"));
        os.flush();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
        }
    }
    public void SaveBitmap(Bitmap bmp){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+
            "sc"+System.currentTimeMillis()+".png");
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            Log.d(TAG,"save bitmap");
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

    Camera.ShutterCallback cameraShutterCallback = new Camera.ShutterCallback()
    {
        @Override
        public void onShutter()
        {
            // TODO Auto-generated method stub
        }
    };

    PictureCallback cameraPictureCallbackRaw = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            // TODO Auto-generated method stub
        }
    };

    PictureCallback cameraPictureCallbackJpeg = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            // TODO Auto-generated method stub
            Bitmap cameraBitmap = BitmapFactory.decodeByteArray
                    (data, 0, data.length);

            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(cameraBitmap,cameraBitmap.getWidth(),cameraBitmap.getHeight(),true);

            cameraBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
            try {

                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+
                        File.separator+"sc"+File.separator+"camera_"+System.currentTimeMillis()+".png");
                cameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                Log.d(TAG,"saved");
                out.flush();
                out.close();
            }
            catch (Exception e){
                Log.d(TAG,e.toString());
            }



//            int   wid = cameraBitmap.getWidth();
//            int  hgt = cameraBitmap.getHeight();
            Bitmap carpet=((BitmapDrawable)imageView1.getDrawable()).getBitmap();
            try {
                Log.d(TAG,"degrees:-"+MultiTouchListener.drg);
                Log.d(TAG,"scale:-"+MultiTouchListener.scale_factor);
//                imageView1.setRotation(MultiTouchListener.drg);
                if(MultiTouchListener.drg!=0) {
                    carpet = RotateBitmap(carpet, MultiTouchListener.drg);
                }
//                Log.d(TAG,"camera width:-"+camera.getParameters().getPictureSize().width);
//                Log.d(TAG,"camera height:-"+camera.getParameters().getPictureSize().height);
//                if(MultiTouchListener.scale_factor!=0) {
//                    Log.d(TAG,"inif");
//                    int scalewidth = (int) (imageView1.getWidth() *MultiTouchListener.scale_factor* MultiTouchListener.scale_factor);
//                    int scaleheight = (int) (imageView1.getHeight() *MultiTouchListener.scale_factor* MultiTouchListener.scale_factor);
//                    carpet = Bitmap.createScaledBitmap(carpet, scalewidth, scaleheight, true);
//                }
//                else{
//                    Log.d(TAG,"inelse");
//                    int scalewidth = (int) (imageView1.getWidth() /3);
//                    int scaleheight = (int) (imageView1.getHeight() /3);
//                    carpet = Bitmap.createScaledBitmap(carpet, scalewidth, scaleheight, true);
////                    if(MultiTouchListener.drg==0){
////                        carpet = RotateBitmap(carpet, 45);
////                    }
//                }


//                carpet = Bitmap.createScaledBitmap(carpet, imageView1.getWidth(), imageView1.getHeight(), true);
//                double scalewidth=carpet.getWidth();
//                double height=carpet.getHeight();
            }
            catch (Exception e){
                Log.d(TAG,e.toString());
            }
//            Bitmap newImage=overlay(cameraBitmap,carpet);
            //  Toast.makeText(getApplicationContext(), wid+""+hgt, Toast.LENGTH_SHORT).show();
            Bitmap newImage = Bitmap.createBitmap
                    (cameraBitmap.getWidth(), cameraBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
            Canvas canvas = new Canvas(newImage);
//
            canvas.drawBitmap(cameraBitmap, 0f, 0f, null);
//
//            Drawable drawable = getResources().getDrawable
//                    (R.mipmap.ic_launcher);
//            Drawable drawable=imageView1.getDrawable();
//            if(MultiTouchListener.scale_factor==0){
//                drawable.setBounds(0,0, drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight() );
//            }
//            else {
//                drawable.setBounds(20, 30, drawable.getIntrinsicWidth() + 20, drawable.getIntrinsicHeight() + 30);
//            }
//            drawable.draw(canvas);
            Drawable d = new BitmapDrawable(getResources(), carpet);
            if(MultiTouchListener.scale_factor==0){
                d.setBounds(0, 0, d.getIntrinsicWidth()-100, d.getIntrinsicHeight()-100);
            }
            else{
                if(MultiTouchListener.scale_factor<0){
                    d.setBounds(20,150, ((int) (d.getIntrinsicWidth() *MultiTouchListener.scale_factor))-100, ((int) (d.getIntrinsicHeight()*MultiTouchListener.scale_factor))-100);
                }
                else {
                    d.setBounds(20,150, ((int) (d.getIntrinsicWidth() *MultiTouchListener.scale_factor))-100, ((int) (d.getIntrinsicHeight()*MultiTouchListener.scale_factor))-100);
                }
            }
            d.draw(canvas);


            File storagePath = new File(Environment.
                    getExternalStorageDirectory() + "/sc/");
            storagePath.mkdirs();
            String system_time=Long.toString(System.currentTimeMillis());
            File myImage = new File(storagePath,
                     system_time+ ".png");

            try
            {
                FileOutputStream out = new FileOutputStream(myImage);
                newImage.compress(Bitmap.CompressFormat.PNG, 100, out);


                out.flush();
                out.close();
                String ImagePath = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        newImage,
                        system_time,
                        system_time
                );

                Uri URI = Uri.parse(ImagePath);
                Toast.makeText(getApplicationContext(),"Image saved to:"+URI.toString(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Main2Activity.this, ImagePreviewActivity.class);
                intent.putExtra("image_url",new File(storagePath,
                        system_time+ ".png").toString());
                startActivity(intent);
                finish();
            }
            catch(FileNotFoundException e)
            {
                Log.d("In Saving File", e + "");
            }
            catch(IOException e)
            {
                Log.d("In Saving File", e + "");
            }

            camera.startPreview();



            newImage.recycle();
            newImage = null;


//            till here
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//
//            intent.setDataAndType(Uri.parse("file://" + myImage.getAbsolutePath()), "image/*");
//            startActivity(intent);

        }
    };
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height)
    {
        // TODO Auto-generated method stub

        if(previewing)
        {
            camera.stopPreview();
            previewing = false;
        }
        try
        {
            boolean pic_size=false;
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPictureSizes();
            for(Camera.Size size:previewSizes){
                Log.d(TAG,"width:-"+size.width+" , height:-"+size.height);
                if(width==1280&&height==720){
                    parameters.setPictureSize(1280,720);
                    pic_size=true;
                }
            }
            if(!pic_size){
                parameters.setPreviewSize(640, 480);
                parameters.setPictureSize(640,480);
            }

//            parameters.setPreviewSize(640, 480);
//            parameters.setPictureSize(1080, 768);
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                camera.setDisplayOrientation(90);

            }

            // parameters.setRotation(90);
            camera.setParameters(parameters);

            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
            previewing = true;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.d(TAG,e.toString());
        }
    }
    public static Bitmap takeScreenshot(View view, Bitmap.Config quality) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), quality);
        Canvas canvas = new Canvas(bitmap);

        Drawable backgroundDrawable = view.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        try
        {
            camera = Camera.open();
        }
        catch(RuntimeException e)
        {
            Toast.makeText(getApplicationContext(), "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }
    public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl)
    {
        int b[]=new int[w*(y+h)];
        int bt[]=new int[w*h];
        IntBuffer ib=IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, 0, w, y+h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

        for(int i=0, k=0; i<h; i++, k++)
        {//remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for(int j=0; j<w; j++)
            {
                int pix=b[i*w+j];
                int pb=(pix>>16)&0xff;
                int pr=(pix<<16)&0x00ff0000;
                int pix1=(pix&0xff00ff00) | pr | pb;
                bt[(h-k-1)*w+j]=pix1;
            }
        }


        Bitmap sb=Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }
}
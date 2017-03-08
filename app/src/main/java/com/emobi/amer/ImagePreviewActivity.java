package com.emobi.amer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class ImagePreviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    private final String TAG=getClass().getSimpleName();
    Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        imageView= (ImageView) findViewById(R.id.iv_image);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String image_url=bundle.getString("image_url");
            Log.d(TAG,"url:-"+image_url);
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(image_url);
//                bitmap=bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,true);
                imageView.setImageBitmap(bitmap);
                image_uri=Uri.parse(image_url);

                MediaScannerConnection.scanFile(this, new String[] { image_url }, new String[] { "image/*" }, null);
            }
            catch (Exception e){
                Log.d(TAG,"error:-"+e.toString());
            }
        }
        else{
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_preview, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_share:
                shareIntent();
//                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareIntent(){
        if(image_uri!=null){
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, image_uri);
            startActivity(Intent.createChooser(share, "Select"));
        }
        else{
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }
}

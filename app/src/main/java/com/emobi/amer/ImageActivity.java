package com.emobi.amer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity {
    @BindView(R.id.iv_url)
    ImageView iv_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String image_url=bundle.getString("image_url");
            Picasso.with(getApplicationContext()).load(image_url).into(iv_url);
        }
    }
}

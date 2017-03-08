package com.emobi.amer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final String TAG=getClass().getSimpleName();

    @BindView(R.id.webview)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        webView.setWebViewClient(new AppWebViewClients(this));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://niceappstore.com/amerrugs");
    }
    public class AppWebViewClients extends WebViewClient {
        private ProgressDialog progressDialog;
        Activity activity;
        public AppWebViewClients(Activity activity) {
            this.activity=activity;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.d(TAG,"url:-"+url);
            if(url.contains(".png")||url.contains(".PNG")||url.contains(".jpg")||url.contains(".JPG")){
                Intent intent=new Intent(MainActivity.this,BasicActivity.class);
                intent.putExtra("image_url",url);
                activity.startActivity(intent);
            }
            else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(webView!=null) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }
}

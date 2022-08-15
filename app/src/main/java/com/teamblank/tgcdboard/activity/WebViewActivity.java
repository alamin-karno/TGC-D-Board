package com.teamblank.tgcdboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.adapter.SyllabusRecyclerAdapter;

public class WebViewActivity extends AppCompatActivity {

    WebView mWebView;
    private TextView titleTV;
    ProgressBar progressBar;
    private String webURL;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        
        init();


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            public void onPageFinished(WebView view, String url) {

                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(WebViewActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        if(!webURL.isEmpty()){
            mWebView.loadUrl(webURL);
        }
        else {
            Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {

        webURL = getIntent().getStringExtra(SyllabusRecyclerAdapter.URL);
        title = getIntent().getStringExtra(SyllabusRecyclerAdapter.TITLE);

        titleTV = findViewById(R.id.titleTV);
        mWebView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        if(title != null){
            titleTV.setText(title);
        }else {
            titleTV.setText("WebView");
        }
    }

    public void back(View view) {
        finish();
    }
}
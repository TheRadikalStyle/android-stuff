/*
 * @author: David Ochoa Gutierrez
 * @contact: @theradikalstyle - david.ochoa.gtz@outlook.com
 * @copyright: TheRadikalSoftware - 2019
 */

package com.theradikalsoftware.examennexusarkus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    WebView webview;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Toast.makeText(this, getResources().getString(R.string.error_detailplace_init), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            url = bundle.getString("url");
            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setSubtitle(url);
            }
            GoToWeb();
        }
    }

    private void GoToWeb(){
        webview = findViewById(R.id.webviewactivity_webview);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return webview.getUrl().equals(url);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        webview = null;
        url = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

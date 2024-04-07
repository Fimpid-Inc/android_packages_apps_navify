package com.fimpid.navify;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    EditText urlInput;

    RelativeLayout urlInputField;
    ImageView clearUrl;
    WebView webView;
    ProgressBar progressBar;

    ImageView webBack, webForward, webRefresh, webShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.url_input);
        clearUrl = findViewById(R.id.clear_icon);
        progressBar = findViewById(R.id.progress_bar);
        webView = findViewById(R.id.web_view);

        webBack = findViewById(R.id.web_back);
        webForward = findViewById(R.id.web_forward);
        webRefresh = findViewById(R.id.web_refresh);
        webShare = findViewById(R.id.web_share);
        urlInputField = findViewById(R.id.input_fields);

        // Set background drawable for ImageView buttons based on system's mode
        setImageViewBackground();

        View decorView = getWindow().getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(visibility);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        loadUrl("douzie.site/look");

        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(urlInput.getWindowToken(), 0);
                    loadUrl(urlInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));

        clearUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlInput.setText("");
            }
        });

        webBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        webForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        webRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        webShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }

    void loadUrl(String url) {
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("google.com/search?q=" + url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    private void setImageViewBackground() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int backgroundDrawableId;
        int editTextBackgroundId = 0;
        int arrowBackDrawableId;
        int arrowForwardDrawableId;
        int cancelDrawableId;
        int refreshDrawableId;
        int shareDrawableId;
        int buttonBackgroundId;

        // Check if it's dark mode or not
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            backgroundDrawableId = R.drawable.bg_imageview_dark;
            editTextBackgroundId = R.drawable.bg_edittext_dark; // Use different drawable for EditText in dark mode
            arrowBackDrawableId = R.drawable.arrow_back_dark;
            arrowForwardDrawableId = R.drawable.arrow_forward_dark;
            cancelDrawableId = R.drawable.cancel_dark;
            refreshDrawableId = R.drawable.refresh_dark;
            shareDrawableId = R.drawable.share_dark;
        } else {
            backgroundDrawableId = R.drawable.bg_imageview;
            arrowBackDrawableId = R.drawable.arrow_back;
            arrowForwardDrawableId = R.drawable.arrow_forward;
            cancelDrawableId = R.drawable.cancel;
            refreshDrawableId = R.drawable.refresh;
            shareDrawableId = R.drawable.share;
        }

        // Set background drawable for ImageView buttons
        webBack.setBackground(ContextCompat.getDrawable(this, backgroundDrawableId));
        webForward.setBackground(ContextCompat.getDrawable(this, backgroundDrawableId));
        webRefresh.setBackground(ContextCompat.getDrawable(this, backgroundDrawableId));
        webShare.setBackground(ContextCompat.getDrawable(this, backgroundDrawableId));
        clearUrl.setBackground(ContextCompat.getDrawable(this, backgroundDrawableId));

        // Set background drawable for ImageView icons
        webBack.setImageDrawable(ContextCompat.getDrawable(this, arrowBackDrawableId));
        webForward.setImageDrawable(ContextCompat.getDrawable(this, arrowForwardDrawableId));
        clearUrl.setImageDrawable(ContextCompat.getDrawable(this, cancelDrawableId));
        webRefresh.setImageDrawable(ContextCompat.getDrawable(this, refreshDrawableId));
        webShare.setImageDrawable(ContextCompat.getDrawable(this, shareDrawableId));

        // Set background drawable for EditText
        urlInputField.setBackground(ContextCompat.getDrawable(this, editTextBackgroundId));
    }
}
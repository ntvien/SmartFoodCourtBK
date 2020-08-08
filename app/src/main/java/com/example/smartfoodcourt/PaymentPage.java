package com.example.smartfoodcourt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PaymentPage extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar progressBarPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mWebView = findViewById(R.id.webView);
        progressBarPayment = findViewById(R.id.progressBarPayment);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient()
                                  {
                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);
                                          mWebView.setVisibility(View.GONE);
                                          progressBarPayment.setVisibility(View.VISIBLE);

                                          if(url.equals("https://www.ashenishanka.com/")){
                                              Toast.makeText(PaymentPage.this, "Thanh toán đã bị hủy", Toast.LENGTH_SHORT).show();
                                              finish();
                                          }
                                          else if(url.equals("https://www.ashenishanka.com/done")){
                                              Toast.makeText(PaymentPage.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                          }
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          super.onPageFinished(view, url);
                                          mWebView.setVisibility(View.VISIBLE);
                                          progressBarPayment.setVisibility(View.GONE);
                                      }
                                  }
        );

        mWebView.loadUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=XGYP5KF5RKWUY");
    }
}
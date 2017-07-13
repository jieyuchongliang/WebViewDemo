package com.example.fj.webviewdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class AndroidJs extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private Button button,start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_js);
        initView();
        initWebview();
    }

    private void initView() {
        button = (Button) findViewById(R.id.call_js);
        button.setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
    }

    private void initWebview() {
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        //允许与js交换
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        webView.loadUrl("file:///android_asset/android_js.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_js:
                callJs();
                break;
            case R.id.start:
                startActivity(new Intent(this,JsCallAndroid.class));
                break;
        }
    }

    private void callJs() {
        // 第一种方法：4.4一下不支持。所以会有api的判断。
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    //此处为 js 返回的结果
//                    Toast.makeText(AndroidJs.this,value, Toast.LENGTH_SHORT).show();
//                }
//
//            });
//        }
//    }
        // 第二种方法：loadUrl
        webView.loadUrl("javascript:callJS()");
        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(AndroidJs.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                result.cancel();//没有这句话，土司只会打印一次。即不会继续调用onJsAlert方法。
                return true;
            }
        });
    }
}

package com.example.fj.webviewdemo;

import android.webkit.JavascriptInterface;

/**
 * Created by 860617010 on 2017/7/13.
 */

public class JsToAndroid extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        System.out.println("JS调用了Android的hello方法" + msg);
    }
}

package com.example.demoapplication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ccic.annotation.compiler.BindJs;
import com.ccic.bros.autojs.helper.AJSHelper;
import com.ccic.bros.autojs.helper.ActivityCallback;

@BindJs(callback = true)
public class AJS extends JSBridgeApi implements ActivityCallback {
    @Override
    public String function() {
        return "a";
    }

    Activity activity;
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    @Override
    public void handle() {
        super.handle();
        AJSHelper.getInstance().startActivity(activity, BActivity.class, this);
    }

    @Override
    public void onSuccess(Intent intent) {
        Log.i("", "");
    }

    @Override
    public void onFailed(Intent intent) {
        Log.i("", "");
    }
}

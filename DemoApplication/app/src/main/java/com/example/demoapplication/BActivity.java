package com.example.demoapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.ccic.bros.autojs.helper.AJSHelper;

public class BActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.a).setOnClickListener(v -> {
            AJSHelper.getInstance().onSuccess(new Intent());
            finish();
        });
    }
}

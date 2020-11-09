package com.example.demoapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ccic.bros.autojs.AutoBindJs;


public class MainActivity extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InvokeFunction invokeFunction = new InvokeFunction();
        addJs(invokeFunction);
        AJS jsBridgeApi = (AJS) invokeFunction.get();
        jsBridgeApi.setActivity(this);
        jsBridgeApi.handle();

    }

    private void addJs(InvokeFunction invokeFunction) {
        AutoBindJs autoBind = new AutoBindJs();
        autoBind.bindJs(invokeFunction);
    }
}

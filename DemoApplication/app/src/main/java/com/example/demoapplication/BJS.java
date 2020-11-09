package com.example.demoapplication;

import com.ccic.annotation.compiler.BindJs;

@BindJs(callback = true)
public class BJS extends JSBridgeApi {
    @Override
    public String function() {
        return "b";
    }
}

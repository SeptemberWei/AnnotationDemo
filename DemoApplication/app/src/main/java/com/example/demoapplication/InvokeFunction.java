package com.example.demoapplication;

import java.util.HashMap;

public class InvokeFunction {
    private HashMap<String, JSBridgeApi> functionMap = new HashMap<>();


    public void addJSApi(JSBridgeApi jsApi) {
        functionMap.put(jsApi.function(), jsApi);
    }

    public JSBridgeApi get(){
        return functionMap.get("a");
    }
}

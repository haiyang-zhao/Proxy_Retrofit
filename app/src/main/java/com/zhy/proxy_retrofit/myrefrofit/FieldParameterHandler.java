package com.zhy.proxy_retrofit.myrefrofit;

public class FieldParameterHandler implements ParameterHandler {
    private final String key;

    public FieldParameterHandler(String key) {
        this.key = key;
    }

    @Override
    public void apply(ServiceMethod serviceMethod, String value) {
        serviceMethod.addFieldParameter(key, value);
    }
}

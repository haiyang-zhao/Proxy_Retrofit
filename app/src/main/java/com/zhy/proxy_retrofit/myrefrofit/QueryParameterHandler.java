package com.zhy.proxy_retrofit.myrefrofit;

public class QueryParameterHandler implements ParameterHandler {
    private final String key;

    public QueryParameterHandler(String key) {
        this.key = key;
    }

    @Override
    public void apply(ServiceMethod serviceMethod, String value) {
        serviceMethod.addQueryParameter(key, value);
    }
}

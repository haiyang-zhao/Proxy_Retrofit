package com.zhy.proxy_retrofit.myrefrofit;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MyRetrofit {

    private final Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();
    private final Call.Factory callFactory;
    private final HttpUrl baseUrl;

    public MyRetrofit(Call.Factory callFactory, HttpUrl baseUrl) {
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
    }

    public Call.Factory getCallFactory() {
        return callFactory;
    }

    public HttpUrl getBaseUrl() {
        return baseUrl;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                (proxy, method, args) -> {
                    ServiceMethod serviceMethod = loadServiceMethod(method);
                    return serviceMethod.invoke(args);
                });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodCache.get(method);
        if (null != serviceMethod) {
            return serviceMethod;
        }
        synchronized (serviceMethodCache) {
            serviceMethod = serviceMethodCache.get(method);
            if (null == serviceMethod) {
                serviceMethod = new ServiceMethod.Builder(this, method).build();
                serviceMethodCache.put(method, serviceMethod);
            }
        }
        return serviceMethod;
    }

    public static final class Builder {
        private Call.Factory callFactory;
        private HttpUrl baseUrl;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = HttpUrl.get(baseUrl);
            return this;
        }

        public MyRetrofit build() {
            if (baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            if (this.callFactory == null) {
                this.callFactory = new OkHttpClient();
            }
            return new MyRetrofit(callFactory, baseUrl);
        }
    }


}

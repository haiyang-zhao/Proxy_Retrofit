package com.zhy.proxy_retrofit.myrefrofit;

import com.zhy.proxy_retrofit.myrefrofit.annotation.Field;
import com.zhy.proxy_retrofit.myrefrofit.annotation.GET;
import com.zhy.proxy_retrofit.myrefrofit.annotation.POST;
import com.zhy.proxy_retrofit.myrefrofit.annotation.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class ServiceMethod {

    private final Call.Factory callFactory;
    private final HttpUrl baseUrl;
    private final String httpMethod;
    private final ParameterHandler[] parameterHandlers;
    private final HttpUrl.Builder urlBuilder;
    private FormBody.Builder formBodyBuilder;

    public ServiceMethod(Call.Factory callFactory,
                         HttpUrl baseUrl, String httpMethod,
                         String url, ParameterHandler[] parameterHandlers) {
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
        this.httpMethod = httpMethod;
        this.parameterHandlers = parameterHandlers;
        this.urlBuilder = this.baseUrl.newBuilder(url);
        if (httpMethod.equals("POST")) {
            this.formBodyBuilder = new FormBody.Builder();
        }
    }


    public Object invoke(Object[] args) {
        for (int i = 0; i < this.parameterHandlers.length; i++) {
            ParameterHandler parameterHandler = this.parameterHandlers[i];
            parameterHandler.apply(this, args[i].toString());
        }
        FormBody formBody = null;
        if (formBodyBuilder != null) {
            formBody = formBodyBuilder.build();
        }
        Request request = new Request.Builder().url(this.urlBuilder.build())
                .method(this.httpMethod, formBody).build();
        return this.callFactory.newCall(request);


    }

    public void addFieldParameter(String key, String value) {
        this.formBodyBuilder.add(key, value);
    }

    public void addQueryParameter(String key, String value) {
        this.urlBuilder.addQueryParameter(key, value);
    }


    public static class Builder {
        private final Annotation[][] parameterAnnotations;
        private MyRetrofit myRetrofit;
        private final Annotation[] methodAnnotations;
        private String httpMethod;
        private String url;
        private ParameterHandler[] parameterHandlers;

        public Builder(MyRetrofit myRetrofit, Method method) {
            this.myRetrofit = myRetrofit;
            this.methodAnnotations = method.getAnnotations();
            this.parameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            //1 解析方法上的注解, 只处理POST与GET
            for (Annotation annotation : this.methodAnnotations) {
                if (annotation instanceof POST) {
                    this.httpMethod = "POST";
                    this.url = ((POST) annotation).value();
                } else if (annotation instanceof GET) {
                    this.httpMethod = "GET";
                    this.url = ((GET) annotation).value();
                }

            }
            //2 解析方法参数的注解
            int length = this.parameterAnnotations.length;
            parameterHandlers = new ParameterHandler[length];
            for (int i = 0; i < length; i++) {
                Annotation[] annotations = this.parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Query) {
                        parameterHandlers[i] = new QueryParameterHandler(((Query) annotation).value());
                    } else if (annotation instanceof Field) {
                        parameterHandlers[i] = new FieldParameterHandler(((Field) annotation).value());
                    }
                }
            }
            return new ServiceMethod(myRetrofit.getCallFactory(),
                    myRetrofit.getBaseUrl(), this.httpMethod, this.url, this.parameterHandlers);
        }

    }
}

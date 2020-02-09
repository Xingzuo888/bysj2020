package com.example.bysj2020.https;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String body = buffer.clone().readString(charset);
        HttpBean result = new Gson().fromJson(body, HttpBean.class);
        //TODO
        synchronized (TokenInterceptor.class) {
            if (result.getState() == -1006) {
                //token过期
                //解除**绑定，并清除对应的数据
            }
        }
        return response;
    }
}

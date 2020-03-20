package com.example.bysj2020.https;

import android.content.Context;
import android.util.Base64;

import com.example.bysj2020.BuildConfig;
import com.example.bysj2020.utils.SpUtil;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ParamsInterceptor implements Interceptor {

    private JsonObject body;
    private Context context;

    public ParamsInterceptor(JsonObject body,Context context) {
        this.body = body;
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        Date current = new Date();

        builder.addHeader("date", getHttpDateHeaderValue(current));
        builder.addHeader("x-ca-timestamp", String.valueOf(current.getTime()));
        builder.addHeader("x-ca-nonce", UUID.randomUUID().toString());
        builder.addHeader("user-agent", "ALIYUN-ANDROID-DEMO");
        builder.addHeader("host", request.url().host());
//        builder.addHeader("x-ca-key", BuildConfig.appKey);
        builder.addHeader("x-ca-version", "1");
        builder.addHeader("content-type", "application/json; charset=UTF-8");
        builder.addHeader("accept", "application/json; charset=utf-8");
        String loginToken = SpUtil.Obtain(context, "loginToken", "").toString();
        if (loginToken.isEmpty()) {
            builder.addHeader("authorization", "");
        } else {
            builder.addHeader("authorization", loginToken);
        }
        if (null != request.body()) {
            builder.addHeader("content-md5", base64AndMD5(body.toString().getBytes()));
        }

        return chain.proceed(builder.build());
    }

    private static String getHttpDateHeaderValue(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd MM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    private static String base64AndMD5(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can not be null");
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.reset();
                md.update(bytes);
                byte[] encodeBytes = Base64.encode(md.digest(), 0);
                byte[] encodeBytes2 = new byte[24];
                for (int i = 0; i < 24; i++) {
                    encodeBytes2[i] = encodeBytes[i];
                }
                return new String(encodeBytes2, Charset.forName("UTF-8"));
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalArgumentException("unknown algorithm MD5");
            }
        }
    }
}

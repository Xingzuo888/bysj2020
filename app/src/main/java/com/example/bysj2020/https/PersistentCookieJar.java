package com.example.bysj2020.https;

import android.content.Context;

import com.example.bysj2020.utils.SpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements CookieJar {

    private Context context;

    public PersistentCookieJar(Context context) {
        this.context = context;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        SpUtil.Save(context, url.host(), new Gson().toJson(cookies));
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        String cookie = SpUtil.Obtain(context, url.host(), "").toString();
        List<Cookie> cookies = new Gson().fromJson(cookie, new TypeToken<List<Cookie>>() {
        }.getType());
        return cookies != null ? cookies : new ArrayList();
    }
}

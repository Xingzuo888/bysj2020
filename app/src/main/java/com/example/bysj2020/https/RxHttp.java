package com.example.bysj2020.https;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.bysj2020.BuildConfig;
import com.example.bysj2020.https.GsonConverter.ResultJsonDeser;
import com.example.bysj2020.https.GsonConverter.StringNullAdapter;
import com.example.bysj2020.utils.SpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxHttp implements LifecycleObserver {

    private Retrofit retrofit;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder builder;
    private Context context;
    private static Gson gson;
    private String method;

    {
        builder = new OkHttpClient.Builder();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeHierarchyAdapter(HttpBean.class, new ResultJsonDeser())
                .registerTypeAdapter(String.class, new StringNullAdapter())
                .create();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.sslSocketFactory(getSSLSocketFactory());
        retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//rx适配器
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());//gson适配器
    }

    /**
     * https支持
     *
     * @return
     */
    private SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /**
     * 增加后台返回""和"null"的处理
     *
     * @return
     */
    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new StringNullAdapter())
                    .create();
        }
        return gson;
    }

    public RxHttp(Context context) {
        this.context = context;
        builder.addInterceptor(new TokenInterceptor(context));
        builder.cookieJar(new PersistentCookieJar(context));
        retrofitBuilder.baseUrl(BuildConfig.bysj2020_url);
    }

    /**
     * post请求
     *
     * @param method
     * @param body
     * @param result
     */
    public void postWithJson(String method, JsonObject body, HttpResult result) {
        //body.addProperty("fromType",2);//登录渠道
        /*body.addProperty("versionCode", BuildConfig.VERSION_CODE);//版本号 1
        body.addProperty("version", BuildConfig.VERSION_NAME);//版本名 "1.0"
        //body.addProperty("channel", AppUtil.getAppMetaData(context, "UMENG_CHANNEL"));//版本来源渠道
        String loginToken = SpUtil.Obtain(context, "loginToken", "").toString();
        if (loginToken.isEmpty() || method.equals("login")) {
            body.addProperty("loginToken", "");
        } else {
            body.addProperty("loginToken", loginToken);
        }*/
        builder.addInterceptor(new ParamsInterceptor(body, context));
        retrofitBuilder.client(builder.build());
        retrofit = retrofitBuilder.build();
        send(method, new Class[]{JsonObject.class}, new JsonObject[]{body}, result);
    }

    /**
     * post分页请求
     *
     * @param method
     * @param body
     * @param result
     */
    public void postPageWithJson(String method, JsonObject body, HttpPageResult result) {
        //body.addProperty("fromType",2);//登录渠道
//        body.addProperty("versionCode", BuildConfig.VERSION_CODE);//版本号 1
//        body.addProperty("version", BuildConfig.VERSION_NAME);//版本名 "1.0"
        //body.addProperty("channel", AppUtil.getAppMetaData(context, "UMENG_CHANNEL"));//版本来源渠道
        /*String loginToken = SpUtil.Obtain(context, "loginToken", "").toString();
        if (loginToken.isEmpty() || method.equals("login")) {
            body.addProperty("loginToken", "");
        } else {
            body.addProperty("loginToken", loginToken);
        }*/
        builder.addInterceptor(new ParamsInterceptor(body, context));
        retrofitBuilder.client(builder.build());
        retrofit = retrofitBuilder.build();
        sendWithPage(method, new Class[]{JsonObject.class}, new JsonObject[]{body}, result);
    }

    /**
     * 网络请求 支持post、get方式，多参数类型
     *
     * @param method
     * @param parameterTypes
     * @param args
     * @param result
     */
    public void send(String method, Class[] parameterTypes, Object[] args, HttpResult result) {
        RxHttpService service = retrofit.create(RxHttpService.class);
        try {
            Observable observable;
            if (parameterTypes == null || parameterTypes.length == 0) {
                observable = (Observable) service.getClass().getMethod(method).invoke(service);
            } else {
                observable = (Observable) service.getClass().getMethod(method, parameterTypes).invoke(service, args);
            }
            observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxHttpObserver(method, result));
            this.method = method;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求分页，支持post、get方式，多参数类型
     *
     * @param method
     * @param parameterTypes
     * @param args
     * @param result
     */
    public void sendWithPage(String method, Class[] parameterTypes, Object[] args, HttpPageResult result) {
        RxHttpService service = retrofit.create(RxHttpService.class);
        try {
            Observable observable;
            if (parameterTypes == null || parameterTypes.length == 0) {
                observable = (Observable) service.getClass().getMethod(method).invoke(service);
            } else {
                observable = (Observable) service.getClass().getMethod(method, parameterTypes).invoke(service, args);
            }
            observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxHttpObserver(method, result));
            this.method = method;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        if (null == method) {
            //取消订阅
            RxApiManager.get().cancel(method);
        }
    }
}

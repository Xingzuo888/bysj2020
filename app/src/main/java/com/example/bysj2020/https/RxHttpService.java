package com.example.bysj2020.https;

import com.example.bysj2020.bean.ImgCodeBean;
import com.example.bysj2020.bean.LoginBean;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RxHttpService {

    //获取登录短信验证码
    @POST("sendLoginSms")
    Observable<HttpBean<String>> sendLoginSms(@Body JsonObject body);

    //短信验证码登录
    @POST("loginPhoneCode")
    Observable<HttpBean<LoginBean>> loginPhoneCode(@Body JsonObject body);

    //密码登录
    @POST("loginAccount")
    Observable<HttpBean<LoginBean>> loginAccount(@Body JsonObject body);

    //获取图片验证码
    @POST("verifyCode")
    Observable<HttpBean<ImgCodeBean>> verifyCode(@Body JsonObject body);

    //获取注册短信验证码
    @POST("sendRegsSms")
    Observable<HttpBean<String>> sendRegsSms(@Body JsonObject body);

    //注册
    @POST("regsMobileCode")
    Observable<HttpBean<String>> regsMobileCode(@Body JsonObject body);

    //获取忘记密码短信验证码
    @POST("passwordOpt/sendSmsForgetPassword")
    Observable<HttpBean<String>> sendSmsForgetPassword(@Body JsonObject body);

    //获取修改密码短信验证码
    @POST("passwordOpt/sendSmsModifyPassword")
    Observable<HttpBean<String>> sendSmsModifyPassword(@Body JsonObject body);

    //忘记密码
    @POST("passwordOpt/forgetPassword")
    Observable<HttpBean<String>> forgetPassword(@Body JsonObject body);

    //修改密码
    @POST("passwordOpt/modifyPassword")
    Observable<HttpBean<String>> modifyPassword(@Body JsonObject body);
}
